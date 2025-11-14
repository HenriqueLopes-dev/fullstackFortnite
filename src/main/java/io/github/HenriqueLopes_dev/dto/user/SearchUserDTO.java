package io.github.HenriqueLopes_dev.dto.user;

import java.util.UUID;

public record SearchUserDTO(
        UUID id,
        String name
) {
}
