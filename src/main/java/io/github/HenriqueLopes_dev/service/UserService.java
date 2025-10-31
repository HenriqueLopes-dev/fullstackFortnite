package io.github.HenriqueLopes_dev.service;

import io.github.HenriqueLopes_dev.model.Userr;
import io.github.HenriqueLopes_dev.repository.UserRepository;
import io.github.HenriqueLopes_dev.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserValidator validator;
    private final PasswordEncoder encoder;

    public void save(Userr user) {
        validator.validate(user);
        String password = user.getPassword();
        user.setPassword(encoder.encode(password));
        repository.save(user);
    }

    public Optional<Userr> getUser(UUID id) {
        return repository.findById(id);
    }

    public void update(Userr user) {
        if (user.getId() == null){
            throw new RuntimeException("Não é possivel atualizar um usuário que não foi cadastrado!");
        }

        validator.validate(user);
        String password = user.getPassword();
        user.setPassword(password);
        repository.save(user);
    }

    public void delete(Userr user) {
        repository.delete(user);
    }

    public Page<Userr> search(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Specification<Userr> specs = Specification.where(null) ;

        return repository.findAll(specs, pageable);
    }
}
