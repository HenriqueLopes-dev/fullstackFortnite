package io.github.HenriqueLopes_dev.dto.cosmetic;

import java.time.LocalDateTime;

public record CosmeticDTO(
        String externalId,
        String name,
        String description,
        String imageUrl,
        String rarity,
        LocalDateTime added,
        String type,
        boolean isNew
) {
}
