package dev.vorstu.dto;

import org.mapstruct.Mapping;

public record StudentRequest(
        @Mapping(target = "id", ignore = true)
        Long id,
        String fio,
        String group,
        String phoneNumber
) {
}
