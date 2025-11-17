package io.github.HenriqueLopes_dev.dto.user;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PurchaseHistoryWithoutUserDTO(
        UUID id,
        Integer price,
        String bundleName,
        String bundleImage,
        List<CosmeticDTO> cosmetics,
        boolean refund,
        LocalDateTime createdAt
) {
}
