package io.github.HenriqueLopes_dev.dto.cosmeticBundle;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;
import io.github.HenriqueLopes_dev.dto.cosmetic.GetCosmeticsOnShopDTO;

import java.util.List;

public record SearchCosmeticBundleDTO(
        String name,
        String imageUrl,
        Integer regularPrice,
        Integer finalPrice,
        List<CosmeticDTO> cosmetics
) {
}
