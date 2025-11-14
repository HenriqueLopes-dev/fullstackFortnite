package io.github.HenriqueLopes_dev.mapper;

import io.github.HenriqueLopes_dev.dto.user.RegisterUserDTO;
import io.github.HenriqueLopes_dev.dto.user.SearchUserDTO;
import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.model.Userr;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Userr toEntity(RegisterUserDTO dto);

    UserDTO toPrivateDTO(Userr user);

    SearchUserDTO toPublicDTO(Userr user);
}
