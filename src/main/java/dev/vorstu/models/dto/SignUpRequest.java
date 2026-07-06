package dev.vorstu.models.dto;

import dev.vorstu.models.entities.Role;

import java.util.List;


public record SignUpRequest(
        String username,
        String password,
        String email,
        Role role,

        String fio,
        String phoneNumber,
        Long groupId,

        String subject,
        List<Long> groupIds
) {


}
