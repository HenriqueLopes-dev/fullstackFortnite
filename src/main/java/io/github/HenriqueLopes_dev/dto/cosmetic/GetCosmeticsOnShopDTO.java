package io.github.HenriqueLopes_dev.dto.cosmetic;

public record GetCosmeticsOnShopDTO(
        String externalId,
        int regularPrice,
        int finalPrice
) {
}
