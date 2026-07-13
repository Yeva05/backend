package dev.vorstu.controllers;

import dev.vorstu.models.dto.RegistrationCsvRow;
import dev.vorstu.models.dto.ValidationResult;
import dev.vorstu.models.dto.group.GroupResponse;
import dev.vorstu.models.entities.*;
import dev.vorstu.services.*;
import dev.vorstu.services.EmailService;
import dev.vorstu.repositories.RegRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registration")
public class EmailController {

    private final CsvParser csvParser;
    private final RegistrationValidator validator;
    private final RegRequestService regRequestService;
    private final EmailService emailService;
    private final UserService userService;
    private final RegRequestRepository regRequestRepository;
    private final GroupService groupService;

    @Autowired
    public EmailController(CsvParser csvParser,
                                  RegistrationValidator validator,
                                  RegRequestService regRequestService,
                                  EmailService emailService,
                                  UserService userService,
                                  RegRequestRepository regRequestRepository,
                                  GroupService groupService) {
        this.csvParser = csvParser;
        this.validator = validator;
        this.regRequestService = regRequestService;
        this.emailService = emailService;
        this.userService = userService;
        this.regRequestRepository = regRequestRepository;
        this.groupService=groupService;
    }

    @PostMapping("/registration-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        // 1. Проверка файла
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Uploaded CSV file is empty or missing.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            // 2. Парсинг CSV
            List<RegistrationCsvRow> rows = csvParser.parseCsv(inputStream);

            // 3. Валидация строк
            List<String> errors = new java.util.ArrayList<>();
            List<RegistrationCsvRow> validRows = new java.util.ArrayList<>();

            for (int i = 0; i < rows.size(); i++) {
                ValidationResult result = validator.validateRow(rows.get(i), i + 2);
                if (result.isValid()) {
                    validRows.add(result.getRow());
                } else {
                    errors.add("Line " + (i + 2) + ": " + String.join(", ", result.getErrors()));
                }
            }

            // 4. Создание заявок через сервис
            List<RegistrationRequest> requests = validRows.stream()
                    .map(regRequestService::createRegistrationRequest)
                    .collect(Collectors.toList());

            // 5. Сохранение всех заявок (сервис может делегировать репозиторию)
            regRequestService.saveAll(requests);

            // 6. Асинхронная отправка писем
            requests.forEach(req -> {
                emailService.sendRegistrationEmail(req.getEmail(), req.getFio(), req.getToken());
                req.setProcessed(true);
                regRequestRepository.save(req);
            });

            return ResponseEntity.ok(Map.of(
                    "successCount", requests.size(),
                    "errors", errors
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to process CSV: " + e.getMessage());
        }
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<?> confirm(@RequestParam String token) {
        // 1. Поиск заявки по токену
        RegistrationRequest request = regRequestRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        // 2. Проверка, что заявка не просрочена и не использована
        if (request.isUsed()) {
            return ResponseEntity.badRequest().body("This registration link has already been used.");
        }
        if (request.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Registration link has expired.");
        }

        User user = new User();
        user.setUsername(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(userService.generateTemporaryPassword());
        String roleStr = request.getRole();
        if (!roleStr.startsWith("ROLE_")) {
            roleStr = "ROLE_" + roleStr;
        }
        user.setRole(Role.valueOf(roleStr));

        if ("STUDENT".equals(request.getRole())) {
            Student student = new Student();
            student.setFio(request.getFio());
            student.setPhoneNumber(request.getPhoneNumber());
            if (request.getGroupId() != null) {
                if (groupService.existsById(request.getGroupId())) {
                    student.setGroup(groupService.getGroupEntityById(request.getGroupId()));
                }
            }
            student.setUser(user);
            user.setStudent(student);
        } else if ("TEACHER".equals(request.getRole())) {
            Teacher teacher = new Teacher();
            teacher.setFio(request.getFio());
            teacher.setPhoneNumber(request.getPhoneNumber());
            teacher.setSubject(request.getSubject());
            teacher.setUser(user);
            user.setTeacher(teacher);
        } else {
            return ResponseEntity.badRequest().body("Unsupported role: " + request.getRole());
        }

        // 4. Сохранение пользователя (каскадно сохранится и дочерняя сущность)
        userService.saveUser(user);

        // 5. Пометка заявки как использованной
        request.setUsed(true);
        regRequestRepository.save(request);

        // 6. Опционально: отправить приветственное письмо с логином/паролем
         emailService.sendWelcomeEmail(user.getEmail(), user.getUsername(), user.getPassword());

        return ResponseEntity.ok("Registration confirmed successfully! You can now log in.");
    }
}