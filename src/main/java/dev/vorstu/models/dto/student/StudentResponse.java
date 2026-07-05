package dev.vorstu.models.dto.student;

public record StudentResponse(
        Long id,
        String fio,
        Long groupId,
        Long userId,
        String phoneNumber
) {
}
