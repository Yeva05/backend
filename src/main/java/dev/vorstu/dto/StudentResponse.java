package dev.vorstu.dto;

public record StudentResponse(
        Long id,
        String fio,
        String group,
        String phoneNumber) {
}
