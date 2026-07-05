package dev.vorstu.models.dto.group;

import dev.vorstu.models.entities.Student;
import dev.vorstu.models.entities.Teacher;

import java.util.List;

public record GroupRequest (
        Long id,
        String name,
        List<Student> studentIds,
        List<Teacher> teacherIds
){
}
