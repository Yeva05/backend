package dev.vorstu.service;

import dev.vorstu.mappers.GroupMapper;
import dev.vorstu.models.dto.group.GroupRequest;
import dev.vorstu.models.dto.group.GroupResponse;
import dev.vorstu.models.entities.Group;
import dev.vorstu.models.entities.Teacher;
import dev.vorstu.repositories.GroupRepository;
import dev.vorstu.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final TeacherRepository teacherRepository;


    public GroupResponse createGroup(GroupRequest groupRequest) {
        Group group=groupMapper.toGroup(groupRequest);
        Group saved = groupRepository.save(group);
        return groupMapper.toGroupResponse(saved);
    }

    public GroupResponse updateGroup(Long id, GroupRequest request) {
        Group existing=groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + id));
        groupMapper.updateEntity(request, existing);
        Group updated =groupRepository.save(existing);
        return groupMapper.toGroupResponse(updated);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    public Page<GroupResponse> getAllGroups(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Group> page = groupRepository.findAll(pageable);
        return page.map(groupMapper::toGroupResponse);
    }

    public GroupResponse getGroupById(Long id) {
        Group group= groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found"));
        return groupMapper.toGroupResponse(group);
    }

    public void assignTeacherToGroup(Long groupId, Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group not found"));

        if (!teacher.getGroups().contains(group)) {
            teacher.getGroups().add(group);
        }
        if (!group.getTeachers().contains(teacher)) {
            group.getTeachers().add(teacher);
        }
        teacherRepository.save(teacher);
    }
}
