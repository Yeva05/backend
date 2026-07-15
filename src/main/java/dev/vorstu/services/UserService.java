package dev.vorstu.services;

import dev.vorstu.models.dto.ChangePasswordRequest;
import dev.vorstu.models.dto.SignUpRequest;
import dev.vorstu.models.dto.UserResponse;
import dev.vorstu.models.entities.*;
import dev.vorstu.mappers.UserMapper;
import dev.vorstu.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

//TODO add the rest of crud methods for the users
@AllArgsConstructor
@Service
public class UserService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final GroupRepository groupRepository;
    private final UserMapper userMapper;


    public UserResponse createUser(SignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already taken");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        if (user.getRole() == Role.ROLE_ADMIN) {
            Admin admin=new Admin();
            admin.setUser(user);
            user.setAdmin(admin);
        }
        else if (user.getRole()==Role.ROLE_TEACHER) {
            if (request.fio() == null || request.fio().isBlank()) {
                throw new IllegalArgumentException("FIO is required for teacher");
            }
            if (request.subject()==null) {
                throw new IllegalArgumentException("Subject is required for teacher");
            }
            Teacher teacher = new Teacher();
            teacher.setFio(request.fio());
            teacher.setPhoneNumber(request.phoneNumber());
            teacher.setSubject(request.subject());
            if (request.groupIds() != null && !request.groupIds().isEmpty()) {
                List<Group> groups = groupRepository.findAllById(request.groupIds());
                if (groups.size() != request.groupIds().size()) {
                    throw new EntityNotFoundException("Some groups not found");
                }
                teacher.setGroups(groups);
            }
            teacher.setUser(user);
            user.setTeacher(teacher);
        }
        else {
            if (request.fio() == null || request.fio().isBlank()) {
                throw new IllegalArgumentException("FIO is required for student");
            }
            Student student = new Student();
            student.setFio(request.fio());
            student.setPhoneNumber(request.phoneNumber());

            if (request.groupId() != null) {
                Group group = groupRepository.findById(request.groupId())
                        .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + request.groupId()));
                student.setGroup(group);
            }
            student.setUser(user);
            user.setStudent(student);
        }
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public String generateTemporaryPassword(int length) {
        if (length < 4) {
            length = 10; // защита от слишком короткого пароля
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    public String generateTemporaryPassword() {
        return generateTemporaryPassword(10);
    }


    public void saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Transactional
    public UserResponse changePassword(User user, ChangePasswordRequest request) {
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        return userMapper.toResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

}

//здесь методы для регистрации, поиска пользователей, управления ролями....