package dev.vorstu.mappers;

import dev.vorstu.models.dto.teacher.TeacherRequest;
import dev.vorstu.models.dto.teacher.TeacherResponse;
import dev.vorstu.models.entities.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherResponse toTeacherResponse(Teacher teacher);
    List<TeacherResponse> toTeacherResponseList(List<Teacher> teachers);

    TeacherRequest toTeacherRequest(Teacher teacher);
    Teacher toTeacher(TeacherRequest teacherRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groups", ignore = true)   // игнорируем
    @Mapping(target = "user", ignore = true)
    void updateEntity(TeacherRequest request, @MappingTarget Teacher teacher);
}
