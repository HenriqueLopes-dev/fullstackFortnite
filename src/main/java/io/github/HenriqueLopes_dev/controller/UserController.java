package io.github.HenriqueLopes_dev.controller;

import io.github.HenriqueLopes_dev.dto.user.RegisterUserDTO;
import io.github.HenriqueLopes_dev.dto.user.SearchUserDTO;
import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.mapper.UserMapper;
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

    // FAZER MOSTRAR TODOS OS COSMETICOS Q A PESSOA TEM
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
                                     @RequestBody RegisterUserDTO dto){

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

}
