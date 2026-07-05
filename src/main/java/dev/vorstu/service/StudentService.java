package dev.vorstu.service;

import dev.vorstu.models.dto.student.StudentRequest;
import dev.vorstu.models.dto.student.StudentResponse;
import dev.vorstu.models.entities.Group;
import dev.vorstu.models.entities.Role;
import dev.vorstu.models.entities.Student;
import dev.vorstu.mappers.StudentMapper;
import dev.vorstu.models.entities.User;
import dev.vorstu.repositories.GroupRepository;
import dev.vorstu.repositories.StudentRepository;
import dev.vorstu.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public StudentResponse createStudent(Long userId, StudentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (user.getStudent() != null || user.getTeacher() != null || user.getAdmin() != null) {
            throw new IllegalStateException("User already has a role assigned");
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
        Student savedStudent = studentRepository.save(student);
        user.setStudent(savedStudent);
        user.setRole(Role.ROLE_STUDENT);
        userRepository.save(user);
        return studentMapper.toStudentResponse(savedStudent);
    }


    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));

        studentMapper.updateEntity(request, existing);

        if (request.groupId() != null) {
            Group group = groupRepository.findById(request.groupId())
                    .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + request.groupId()));
            existing.setGroup(group);
        }
        Student updated = studentRepository.save(existing);
        return studentMapper.toStudentResponse(updated);
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        User user = student.getUser();
        user.setRole(null);
        user.setStudent(null);
        studentRepository.deleteById(id);
    }

    public Page<StudentResponse> getAllStudents(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Student> page = studentRepository.findAll(pageable);
         return page.map(studentMapper::toStudentResponse);
    }

    public StudentResponse getStudentById(Long id) {
        Student student= studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return studentMapper.toStudentResponse(student);
    }

}
