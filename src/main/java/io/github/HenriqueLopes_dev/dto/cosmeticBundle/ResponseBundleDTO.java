package io.github.HenriqueLopes_dev.dto.cosmeticBundle;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;

import java.util.List;
import java.util.UUID;


public record ResponseBundleDTO(
        UUID id,
        String name,
        String imageUrl,
        Integer regularPrice,
        Integer finalPrice,
        Boolean isOnSale,
        List<CosmeticDTO> cosmetics
) {
}
