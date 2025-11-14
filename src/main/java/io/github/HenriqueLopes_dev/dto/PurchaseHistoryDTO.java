package io.github.HenriqueLopes_dev.dto;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PurchaseHistoryDTO(
        UUID id,
        Integer price,
        String bundleName,
        String bundleImage,
        List<CosmeticDTO> cosmetics,
        boolean isRefound,
        LocalDateTime createdAt
) {
}
