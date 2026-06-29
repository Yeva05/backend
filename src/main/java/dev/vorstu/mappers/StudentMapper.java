package dev.vorstu.mappers;

import dev.vorstu.dto.StudentResponse;
import dev.vorstu.entities.Student;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponse toStudentResponse(Student student);
    List<StudentResponse> toStudentResponseList(List<Student> students);
}


