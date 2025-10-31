package io.github.HenriqueLopes_dev.mapper;

import io.github.HenriqueLopes_dev.dto.user.RegisterUserDTO;
import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.model.Userr;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    Userr toEntity(RegisterUserDTO dto);

    UserDTO toDTO(Userr user);
}
