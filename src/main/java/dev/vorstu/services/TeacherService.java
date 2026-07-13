package dev.vorstu.services;

import dev.vorstu.mappers.TeacherMapper;
import dev.vorstu.models.dto.teacher.TeacherRequest;
import dev.vorstu.models.dto.teacher.TeacherResponse;
import dev.vorstu.models.entities.*;
import dev.vorstu.repositories.GroupRepository;
import dev.vorstu.repositories.TeacherRepository;
import dev.vorstu.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TeacherService {
    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public TeacherResponse createTeacher(Long userId, TeacherRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (user.getStudent() != null || user.getTeacher() != null || user.getAdmin() != null) {
            throw new IllegalStateException("User already has a role assigned");
        }

        Teacher teacher = new Teacher();
        teacher.setFio(request.fio());
        teacher.setPhoneNumber(request.phoneNumber());

        teacher.setUser(user);

        Teacher savedTeacher = teacherRepository.save(teacher);

        user.setTeacher(savedTeacher);
        user.setRole(Role.ROLE_STUDENT);

        userRepository.save(user);

      return teacherMapper.toTeacherResponse(savedTeacher);
    }


    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        teacherMapper.updateEntity(request, existing);

        if (request.groupIds() != null) {
            List<Group> groups = groupRepository.findAllById(request.groupIds());
            if (groups.size() != request.groupIds().size()) {
                throw new EntityNotFoundException("Some groups not found");
            }
            existing.setGroups(groups);
        }

        if (request.userId() != null) {
            User user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            existing.setUser(user);
        }

        Teacher updated = teacherRepository.save(existing);
        return teacherMapper.toTeacherResponse(updated);
    }

    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        User user = teacher.getUser();
        user.setRole(null);
        user.setTeacher(null);
        teacherRepository.deleteById(id);
    }

    public Page<TeacherResponse> getAllTeachers() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Teacher> page = teacherRepository.findAll(pageable);
        return page.map(teacherMapper::toTeacherResponse);
    }

    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        return teacherMapper.toTeacherResponse(teacher);
    }

    @Transactional
    public TeacherResponse assignTeacherToGroup(Long groupId, Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group not found"));

        if (!teacher.getGroups().contains(group)) {
            teacher.getGroups().add(group);
        }
        if (!group.getTeachers().contains(teacher)) {
            group.getTeachers().add(teacher);
        }
        teacherRepository.save(teacher);
        return teacherMapper.toTeacherResponse(teacher);
    }


}