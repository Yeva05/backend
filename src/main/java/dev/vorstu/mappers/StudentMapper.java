package dev.vorstu.mappers;

import dev.vorstu.models.dto.student.StudentRequest;
import dev.vorstu.models.dto.student.StudentResponse;
import dev.vorstu.models.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponse toStudentResponse(Student student);
    List<StudentResponse> toStudentResponseList(List<Student> students);

    StudentRequest toStudentRequest(Student student);
    Student toStudent(StudentRequest studentRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target="group", ignore = true)
    void updateEntity(StudentRequest request, @MappingTarget Student student);

}


