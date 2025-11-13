package io.github.HenriqueLopes_dev.mapper;

import io.github.HenriqueLopes_dev.dto.cosmeticBundle.CosmeticBundleDTO;
import io.github.HenriqueLopes_dev.model.CosmeticBundle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CosmeticBundleMapper {
    CosmeticBundle toEntity(CosmeticBundleDTO dto);


}
