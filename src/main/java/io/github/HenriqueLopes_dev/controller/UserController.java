package io.github.HenriqueLopes_dev.controller;

import io.github.HenriqueLopes_dev.dto.user.PurchaseHistoryWithoutUserDTO;
import io.github.HenriqueLopes_dev.dto.user.RegisterUserDTO;
import io.github.HenriqueLopes_dev.dto.user.SearchUserDTO;
import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.mapper.UserMapper;
import io.github.HenriqueLopes_dev.model.PurchaseHistory;
import io.github.HenriqueLopes_dev.model.Userr;
import io.github.HenriqueLopes_dev.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController implements GenericController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid RegisterUserDTO dto, HttpServletResponse response){

        Userr entity = mapper.toEntity(dto);
        service.save(entity);

        URI uri = generateHeaderLocation(entity.getId());

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> read(@PathVariable String id){

        Optional<Userr> opUser = service.getUser(UUID.fromString(id));

        if (opUser.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Userr targetUser = opUser.get();

        if (isOwner(targetUser.getId())) {
            return ResponseEntity.ok(mapper.toPrivateDTO(targetUser));
        }
        return ResponseEntity.ok(mapper.toPublicDTO(targetUser));
    }


    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable String id,
                                         @RequestBody @Valid RegisterUserDTO dto){

        return service.getUser(UUID.fromString(id))
                .map(user -> {

                    if (!isOwner(user.getId())){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }

                    user.setName(dto.name());
                    user.setEmail(dto.email());
                    user.setPassword(dto.password());
                    service.update(user);

                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable String id){

        Optional<Userr> opUser = service.getUser(UUID.fromString(id));

        if (opUser.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Userr user = opUser.get();

        if (!isOwner(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.delete(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<SearchUserDTO>> search(
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "50")
            Integer pageSize
    ){

        Page<Userr> pageResult = service.search(page, pageSize);

        Page<SearchUserDTO> finalDTO = pageResult.map(mapper::toPublicDTO);

        return ResponseEntity.ok(finalDTO);
    }

    @GetMapping("{id}/purchase-history")
    public ResponseEntity<Page<PurchaseHistoryWithoutUserDTO>> getPurchaseHistory(
            @PathVariable String id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize) {

        Optional<Userr> opUser = service.getUser(UUID.fromString(id));

        if (opUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Userr targetUser = opUser.get();

        if (isOwner(targetUser.getId())) {
            Page<PurchaseHistory> pageResult = service.getPurchaseHistoryByUser(targetUser, page, pageSize);
            Page<PurchaseHistoryWithoutUserDTO> finalDTO = pageResult.map(mapper::noUserToDTO);
            return ResponseEntity.ok(finalDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}/acquired-cosmetics")
    public ResponseEntity<Page<PurchaseHistoryWithoutUserDTO>> getAcquiredCosmetics(
            @PathVariable String id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize) {

        Optional<Userr> opUser = service.getUser(UUID.fromString(id));

        if (opUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Userr targetUser = opUser.get();

        if (isOwner(targetUser.getId())) {
            Page<PurchaseHistory> pageResult = service.getAcquiredCosmeticsByUser(targetUser, page, pageSize);
            Page<PurchaseHistoryWithoutUserDTO> finalDTO = pageResult.map(mapper::noUserToDTO);
            return ResponseEntity.ok(finalDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("me/acquired-cosmetics/{id}/refund")
    public ResponseEntity<Void> refund(@PathVariable String id) {

        Userr currentUser = getCurrentUser();

        Optional<PurchaseHistory> opPHistory = service.getPurchaseHistoryByIdAndUser(
                UUID.fromString(id),
                currentUser
        );

        if (opPHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PurchaseHistory purchaseHistory = opPHistory.get();

        if (purchaseHistory.isRefund()) {
            return ResponseEntity.badRequest().build();
        }

        service.refundPurchase(purchaseHistory);
        return ResponseEntity.noContent().build();
    }

}
