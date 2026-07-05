package dev.vorstu.models.dto.student;

public record StudentRequest(
        Long id,
        String fio,
        Long groupId,
        Long userId,
        String phoneNumber
) {
}
