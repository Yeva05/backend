package dev.vorstu.models.dto.admin;

import dev.vorstu.models.entities.User;

public record AdminRequest(
        Long id,
        User userId
) {
}
