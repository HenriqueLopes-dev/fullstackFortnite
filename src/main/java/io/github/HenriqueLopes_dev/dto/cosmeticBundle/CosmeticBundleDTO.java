package io.github.HenriqueLopes_dev.dto.cosmeticBundle;

import io.github.HenriqueLopes_dev.dto.cosmetic.GetCosmeticsOnShopDTO;

import java.util.List;

public record CosmeticBundleDTO(
        String name,
        String imageUrl,
        Integer regularPrice,
        Integer finalPrice,
        List<GetCosmeticsOnShopDTO> cosmeticDTOs
) {
}
