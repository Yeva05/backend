package dev.vorstu.models.dto.auth;

import lombok.Getter;
import org.jspecify.annotations.Nullable;


public record AuthRequest(
        @Getter
        String username,
        @Getter
        String password) {
}
