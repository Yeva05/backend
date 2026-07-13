package dev.vorstu.services;

import dev.vorstu.models.dto.RegistrationCsvRow;
import dev.vorstu.models.entities.RegistrationRequest;
import dev.vorstu.repositories.RegRequestRepository;
import dev.vorstu.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import dev.vorstu.repositories.GroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegRequestService {

    private final RegRequestRepository regRequestRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;


    @Value("${registration.link.expiry.hours:24}")
    private long expiryHours;

    public RegRequestService(RegRequestRepository registrationRequestRepository,
                             UserRepository userRepository,
                             GroupRepository groupRepository) {
        this.regRequestRepository = registrationRequestRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }


    public RegistrationRequest createRegistrationRequest(RegistrationCsvRow row) {
        // Проверка: не зарегистрирован ли уже пользователь с таким email
        if (userRepository.existsByEmail(row.getEmail())) {
            throw new IllegalArgumentException("User with email " + row.getEmail() + " already registered");
        }

        // Проверка: нет ли уже активной заявки с таким email
        Optional<RegistrationRequest> existing = regRequestRepository
                .findByEmailAndUsedFalseAndExpiryDateAfter(row.getEmail(), LocalDateTime.now());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Registration request for email " + row.getEmail() + " already exists");
        }

        // Если студент – проверяем существование группы
        if ("STUDENT".equals(row.getRole()) && row.getGroupId() != null && !row.getGroupId().isBlank()) {
            Long groupId;
            try {
                groupId = Long.parseLong(row.getGroupId());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid groupId format: " + row.getGroupId());
            }
            if (!groupRepository.existsById(groupId)) {
                throw new IllegalArgumentException("Group with id " + groupId + " does not exist");
            }
        }

        // Создание заявки
        RegistrationRequest request = new RegistrationRequest();
        request.setUserName(row.getUserName());
        request.setEmail(row.getEmail());
        request.setRole(row.getRole());
        request.setFio(row.getFio());
        request.setPhoneNumber(row.getPhoneNumber());

        if (row.getGroupId() != null && !row.getGroupId().isBlank()) {
            request.setGroupId(Long.parseLong(row.getGroupId()));
        }
        request.setSubject(row.getSubject());

        String token = UUID.randomUUID().toString();
        request.setToken(token);
        request.setCreatedAt(LocalDateTime.now());
        request.setExpiryDate(LocalDateTime.now().plusHours(expiryHours));
        request.setUsed(false);
        request.setProcessed(false);

        return regRequestRepository.save(request);
    }

    public void saveAll(List<RegistrationRequest> requests) {
    }
}