package dev.vorstu.mappers;

import dev.vorstu.dto.StudentRequest;
import dev.vorstu.dto.StudentResponse;
import dev.vorstu.entities.Student;
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
    void updateEntity(StudentRequest request, @MappingTarget Student student);

}


