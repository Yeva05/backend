package dev.vorstu.models.dto.teacher;

import java.util.List;

public record TeacherResponse(
        Long id,
        String fio,
        List<Long> groupIds,
        Long userId,
        String subject
        ) {
}
