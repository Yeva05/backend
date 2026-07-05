package dev.vorstu.models.dto;

import dev.vorstu.models.entities.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        Long studentId,
        Long teacherId,
        Long adminId
) {
}
