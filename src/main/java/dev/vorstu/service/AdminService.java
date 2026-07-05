package dev.vorstu.service;

import dev.vorstu.mappers.AdminMapper;
import dev.vorstu.models.dto.admin.AdminRequest;
import dev.vorstu.models.dto.admin.AdminResponse;
import dev.vorstu.models.entities.Admin;
import dev.vorstu.models.entities.Role;
import dev.vorstu.models.entities.User;
import dev.vorstu.repositories.AdminRepository;
import dev.vorstu.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public AdminResponse createAdmin(Long userId, AdminRequest AdminRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (user.getStudent() != null || user.getTeacher() != null || user.getAdmin() != null) {
            throw new IllegalStateException("User already has a role assigned");
        }
        
        Admin admin=new Admin();
        admin.setUser(user);
        Admin savedAdmin=adminRepository.save(admin);
        user.setAdmin(savedAdmin);
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
        
        return adminMapper.toAdminResponse(savedAdmin);
    }

    public AdminResponse updateAdmin(Long id, AdminRequest request) {
        Admin existing=adminRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
        adminMapper.updateEntity(request, existing);
        Admin updated =adminRepository.save(existing);
        return adminMapper.toAdminResponse(updated);
    }

    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        User user = admin.getUser();
        user.setRole(null);
        user.setAdmin(null);
        adminRepository.deleteById(id);
    }

    public Page<AdminResponse> getAllAdmins(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Admin> page = adminRepository.findAll(pageable);
        return page.map(adminMapper::toAdminResponse);
    }

    public AdminResponse getAdminById(Long id) {
        Admin Admin= adminRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        return adminMapper.toAdminResponse(Admin);
    }
    
}
