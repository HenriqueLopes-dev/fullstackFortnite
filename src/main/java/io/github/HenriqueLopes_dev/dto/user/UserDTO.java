package io.github.HenriqueLopes_dev.dto.user;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email,
        int balance
) {
}
