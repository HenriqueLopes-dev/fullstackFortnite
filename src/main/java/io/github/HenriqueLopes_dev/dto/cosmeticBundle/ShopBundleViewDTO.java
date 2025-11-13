package io.github.HenriqueLopes_dev.dto.cosmeticBundle;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;

import java.util.List;

public record ShopBundleViewDTO(
        String bundleId,
        String bundleName,
        String bundleImageUrl,
        Integer regularPrice,
        Integer finalPrice,
        List<CosmeticDTO> cosmetics
) {}
