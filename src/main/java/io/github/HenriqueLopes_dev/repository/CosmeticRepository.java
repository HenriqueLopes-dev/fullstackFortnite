package io.github.HenriqueLopes_dev.repository;

import io.github.HenriqueLopes_dev.model.Cosmetic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CosmeticRepository extends JpaRepository<Cosmetic, UUID>, JpaSpecificationExecutor<Cosmetic> {

    Optional<Cosmetic> findByExternalId(String externalId);

    @Modifying
    @Query("""
        UPDATE Cosmetic c
        SET c.isNew = false,
            c.bundle = NULL
    """)
    void updateAllByDefault();

    boolean existsByExternalId(String externalId);

    List<Cosmetic> findAllByExternalIdIn(List<String> externalIds);

}
