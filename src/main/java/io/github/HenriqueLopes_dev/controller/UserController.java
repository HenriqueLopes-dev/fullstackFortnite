package io.github.HenriqueLopes_dev.controller;

import io.github.HenriqueLopes_dev.dto.user.RegisterUserDTO;
import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.mapper.UserMapper;
import io.github.HenriqueLopes_dev.model.Userr;
import io.github.HenriqueLopes_dev.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController implements GenericController{

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid RegisterUserDTO dto){

        Userr entity = mapper.toEntity(dto);
        service.save(entity);

        URI uri = generateHeaderLocation(entity.getId());

        return ResponseEntity.created(uri).build();
    }

    // FAZER VALIDACAO SE É O USUARIO VENDO AS PROPRIAS INFO
    // FAZER MOSTRAR TODOS OS COSMETICOS Q A PESSOA TEM
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> read(@PathVariable String id){

        Optional<Userr> user = service.getUser(UUID.fromString(id));

        if (user.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        UserDTO dto = mapper.toDTO(user.get());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable String id,
                                         @RequestBody RegisterUserDTO dto){

        return service.getUser(UUID.fromString(id))
                .map(user -> {
                    user.setName(dto.name());
                    user.setEmail(dto.email());
                    user.setPassword(dto.password());
                    service.update(user);

                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable String id){

        Optional<Userr> user = service.getUser(UUID.fromString(id));

        if (user.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        service.delete(user.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedModel<UserDTO>> search(
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "50")
            Integer pageSize,
            PagedResourcesAssembler<UserDTO> assembler
    ){

        Page<Userr> pageResult = service.search(page, pageSize);

        Page<UserDTO> finalDTO = pageResult.map(mapper::toDTO);

        return ResponseEntity.ok(assembler.toModel(finalDTO));
    }

}
