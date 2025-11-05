package io.github.HenriqueLopes_dev.dto.cosmetic;

public record CosmeticDTO(
        String externalId,
        String name,
       String description,
       String imageUrl,
       String rarity,
       String added,
       String type,
       boolean isNew,
       boolean onShop,
       int regularPrice,
       int finalPrice
) {
}
