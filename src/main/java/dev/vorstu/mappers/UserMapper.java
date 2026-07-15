package dev.vorstu.mappers;

import dev.vorstu.models.dto.SignUpRequest;
import dev.vorstu.models.dto.UserResponse;
import dev.vorstu.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "admin", ignore = true)
    User toUser(SignUpRequest request);

    @Mapping(target = "studentId", expression = "java(user.getStudent() != null ? user.getStudent().getId() : null)")
    @Mapping(target = "teacherId", expression = "java(user.getTeacher() != null ? user.getTeacher().getId() : null)")
    @Mapping(target = "adminId", expression = "java(user.getAdmin() != null ? user.getAdmin().getId() : null)")
    UserResponse toResponse(User user);


}
