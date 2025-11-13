package io.github.HenriqueLopes_dev.dto.cosmetic;

import io.github.HenriqueLopes_dev.dto.cosmeticBundle.CosmeticBundleInfosDTO;

public record SearchCosmeticDTO(
        String externalId,
        String name,
        String description,
        String imageUrl,
        String type,
        String rarity,
        String added,
        Boolean isNew,
        CosmeticBundleInfosDTO bundle,
        Integer regularPrice,
        Integer finalPrice
) {
}
