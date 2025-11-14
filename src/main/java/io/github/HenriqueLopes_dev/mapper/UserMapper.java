package io.github.HenriqueLopes_dev.mapper;

import io.github.HenriqueLopes_dev.dto.PurchaseHistoryDTO;
import io.github.HenriqueLopes_dev.dto.user.RegisterUserDTO;
import io.github.HenriqueLopes_dev.dto.user.SearchUserDTO;
import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.model.PurchaseHistory;
import io.github.HenriqueLopes_dev.model.Userr;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Userr toEntity(RegisterUserDTO dto);

    UserDTO toPrivateDTO(Userr user);

    SearchUserDTO toPublicDTO(Userr user);

    List<PurchaseHistoryDTO> toDTO(List<PurchaseHistory> purchaseHistoryList);
}
