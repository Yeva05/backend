package dev.vorstu.service;

import dev.vorstu.models.dto.student.StudentRequest;
import dev.vorstu.models.dto.student.StudentResponse;
import dev.vorstu.models.entities.*;
import dev.vorstu.mappers.StudentMapper;
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

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;


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


    public StudentResponse updateStudent(User currentUser, Long id, StudentRequest request) throws AccessDeniedException {
        Role role = currentUser.getRole();
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        if (role==role.ROLE_STUDENT) {
            Long currentStudentId=existing.getId();
            if (!currentStudentId.equals(id)) {
                throw new AccessDeniedException("Студенты могут менять только свой профиль");
            }
        }
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

    public Page<StudentResponse> getAllStudents(User currentUser, Pageable pageable){
        if (pageable == null) {
            pageable = PageRequest.of(0, 10, Sort.by("id"));
        }

        Page<Student> page;

        Role role = currentUser.getRole();
        if (role == Role.ROLE_ADMIN) {
            page = studentRepository.findAll(pageable);
        } else if (role == Role.ROLE_TEACHER) {
            Teacher teacher = currentUser.getTeacher();
            if (teacher == null || teacher.getGroups().isEmpty()) {
                page = Page.empty(pageable);
            } else {
                List<Long> groupIds = teacher.getGroups().stream()
                        .map(Group::getId)
                        .collect(Collectors.toList());
                page = studentRepository.findByGroupIdIn(groupIds, pageable);
            }
        } else if (role == Role.ROLE_STUDENT) {
            Student student = currentUser.getStudent();
            if (student == null) {
                page = Page.empty(pageable);
            } else {
                Long groupId = student.getGroup() != null ? student.getGroup().getId() : null;
                if (groupId == null) {
                    page = studentRepository.findByIdIn(List.of(student.getId()), pageable);
                } else {
                    page = studentRepository.findByGroupId(groupId, pageable);
                }
            }
        } else {
            page = Page.empty(pageable);
        }

        return page.map(studentMapper::toStudentResponse);
    }

    public StudentResponse getStudentById(User currentUser, Long id) throws AccessDeniedException {
        Student student= studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Role role = currentUser.getRole();
        if (role==Role.ROLE_TEACHER) {
            Teacher teacher = currentUser.getTeacher();
            if (teacher == null) {
                throw new AccessDeniedException("Teacher profile not found");
            }
            List<Long> teacherGroupIds = teacher.getGroups().stream()
                    .map(Group::getId)
                    .collect(Collectors.toList());
            if (teacherGroupIds.isEmpty()) {
                throw new AccessDeniedException("Teacher has no groups");
            }
            Long studentGroupId = student.getGroup() != null ? student.getGroup().getId() : null;
            if (studentGroupId == null || !teacherGroupIds.contains(studentGroupId)) {
                throw new AccessDeniedException("Student is not in your group");
            }
        }
        else if (role==Role.ROLE_STUDENT) {
            Student currentStudent = currentUser.getStudent();
            if (currentStudent == null) {
                throw new AccessDeniedException("Student profile not found");
            }
            if (!currentStudent.getId().equals(student.getId())) {
                throw new AccessDeniedException("You can view only your own profile");
            }
        }
        return studentMapper.toStudentResponse(student);
    }

}
