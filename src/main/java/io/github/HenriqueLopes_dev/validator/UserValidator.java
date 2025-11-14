package io.github.HenriqueLopes_dev.validator;

import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.exception.DuplicatedRegistryException;
import io.github.HenriqueLopes_dev.model.Userr;
import io.github.HenriqueLopes_dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository repository;

    public void validate(Userr user){
        if (hasUserWithEmail(user)){
            throw new DuplicatedRegistryException("Já existe um usuário com este email já cadastrado!");
        }
    }

    private boolean hasUserWithEmail(Userr user) {
        return repository.findByEmail(user.getEmail())
                .map(userr -> !userr.getId().equals(user.getId()) )
                .orElse(false);
    }

    public void validateSameUser(Userr user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Userr userr) {

            if (userr.getId() == user.getId()){
                return;
            }
        }
        throw new DuplicatedRegistryException("Você não pode acessar este conteúdo!");
    }
}
