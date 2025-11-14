package io.github.HenriqueLopes_dev.mapper;

import io.github.HenriqueLopes_dev.dto.cosmetic.SearchCosmeticDTO;
import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;
import io.github.HenriqueLopes_dev.dto.cosmeticBundle.CosmeticBundleDTO;
import io.github.HenriqueLopes_dev.dto.cosmeticBundle.ResponseBundleDTO;
import io.github.HenriqueLopes_dev.dto.cosmeticBundle.SearchCosmeticBundleDTO;
import io.github.HenriqueLopes_dev.model.Cosmetic;
import io.github.HenriqueLopes_dev.model.CosmeticBundle;
import io.github.HenriqueLopes_dev.model.CosmeticBundleRelation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CosmeticMapper {
    Cosmetic toEntity(CosmeticDTO dto);

    void updateEntityFromDto(CosmeticDTO dto, @MappingTarget Cosmetic cosmetic);

    CosmeticDTO toDTO(Cosmetic cosmetic);

    SearchCosmeticBundleDTO bundleToDTO(Cosmetic cosmetic);

    public default ResponseBundleDTO responseBundleToDTO(CosmeticBundle bundle) {
        if (bundle == null) {
            return null;
        }

        List<CosmeticDTO> cosmeticDTOs = new ArrayList<>();
        if (bundle.getCosmetics() != null && !bundle.getCosmetics().isEmpty()) {
            cosmeticDTOs = bundle.getCosmetics().stream()
                    .map(CosmeticBundleRelation::getCosmetic)
                    .filter(Objects::nonNull)
                    .map(this::cosmeticToDTO)
                    .collect(Collectors.toList());
        }

        return new ResponseBundleDTO(
                bundle.getId(),
                bundle.getName(),
                bundle.getImageUrl(),
                bundle.getRegularPrice(),
                bundle.getFinalPrice(),
                bundle.getIsOnSale(),
                cosmeticDTOs
        );
    }

    private CosmeticDTO cosmeticToDTO(Cosmetic cosmetic) {
        if (cosmetic == null) {
            return null;
        }

        return new CosmeticDTO(
                cosmetic.getExternalId(),
                cosmetic.getName(),
                cosmetic.getDescription(),
                cosmetic.getImageUrl(),
                cosmetic.getRarity(),
                cosmetic.getAdded(),
                cosmetic.getType(),
                cosmetic.getIsNew()
        );
    }


}
