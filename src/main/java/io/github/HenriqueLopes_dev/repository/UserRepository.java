package io.github.HenriqueLopes_dev.repository;

import io.github.HenriqueLopes_dev.model.Userr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Userr, UUID>, JpaSpecificationExecutor<Userr> {

    Optional<Userr> findByEmail(String username);
}
