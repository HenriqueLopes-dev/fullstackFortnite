package io.github.HenriqueLopes_dev.validator;

import io.github.HenriqueLopes_dev.model.Userr;
import io.github.HenriqueLopes_dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository repository;

    public void validate(Userr user){

    }
}
