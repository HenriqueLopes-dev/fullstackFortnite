package io.github.HenriqueLopes_dev.mapper;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;
import io.github.HenriqueLopes_dev.dto.user.RegisterUserDTO;
import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.model.Cosmetic;
import io.github.HenriqueLopes_dev.model.Userr;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CosmeticMapper {
    Cosmetic toEntity(CosmeticDTO dto);

    void updateEntityFromDto(CosmeticDTO dto, @MappingTarget Cosmetic cosmetic);
}
