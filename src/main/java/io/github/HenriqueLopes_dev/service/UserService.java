package io.github.HenriqueLopes_dev.service;

import io.github.HenriqueLopes_dev.model.PurchaseHistory;
import io.github.HenriqueLopes_dev.model.Userr;
import io.github.HenriqueLopes_dev.repository.PurchaseHistoryRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PurchaseHistoryRepository pHRepository;
    private final UserValidator validator;
    private final PasswordEncoder encoder;

    public void save(Userr user) {
        validator.validate(user);

        if (user.getRole() == null) {
            user.setRole("ROLE_USER");
        }

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

    public Optional<Userr> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Userr authenticate(String email, String password) {
        Optional<Userr> usuario = repository.findByEmail(email);

        if (usuario.isEmpty() || !encoder.matches(password, usuario.get().getPassword())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        return usuario.get();
    }

    public void validateReadInfo(Userr user) {
        validator.validateSameUser(user);
    }

    public List<PurchaseHistory> filterByNotIsRefound(Userr targetUser) {
        return targetUser.getPurchaseHistory().stream().map(c -> {
            if (!c.isRefound()){
                return c;
            }
            return null;
        }).toList();
    }
}
