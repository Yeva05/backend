package dev.vorstu.models.dto.admin;

import dev.vorstu.models.entities.User;

public record AdminResponse (
        Long id,
        User userId
){
}
