package io.github.HenriqueLopes_dev.dto.cosmeticBundle;

public record CosmeticBundleInfosDTO(
        String name,
        String imageUrl,
        Integer regularPrice,
        Integer finalPrice
) {
}
