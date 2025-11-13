package io.github.HenriqueLopes_dev.mapper;

import io.github.HenriqueLopes_dev.dto.cosmetic.SearchCosmeticDTO;
import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;
import io.github.HenriqueLopes_dev.dto.cosmeticBundle.SearchCosmeticBundleDTO;
import io.github.HenriqueLopes_dev.model.Cosmetic;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CosmeticMapper {
    Cosmetic toEntity(CosmeticDTO dto);

    void updateEntityFromDto(CosmeticDTO dto, @MappingTarget Cosmetic cosmetic);

    CosmeticDTO toDTO(Cosmetic cosmetic);

    SearchCosmeticBundleDTO bundleToDTO(Cosmetic cosmetic);
}
