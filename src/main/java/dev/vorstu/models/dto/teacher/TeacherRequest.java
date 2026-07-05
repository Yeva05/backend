package dev.vorstu.models.dto.teacher;

import dev.vorstu.models.entities.User;

import java.util.List;

public record TeacherRequest(
        Long id,
        String fio,
        String phoneNumber,
        List<Long> groupIds,
        Long userId,
        User user,
        String subject
) {
}
