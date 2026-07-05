package dev.vorstu.service;

import dev.vorstu.models.dto.SignUpRequest;
import dev.vorstu.models.dto.UserResponse;
import dev.vorstu.models.entities.User;
import dev.vorstu.mappers.UserMapper;
import dev.vorstu.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

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
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(null);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

}

//здесь методы для регистрации, поиска пользователей, управления ролями....