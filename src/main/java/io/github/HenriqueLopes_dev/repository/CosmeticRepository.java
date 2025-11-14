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
    @Query(value = "UPDATE cosmetic SET is_new = false", nativeQuery = true)
    void updateAllByDefault();

    @Modifying
    @Query(value = "TRUNCATE TABLE cosmetic_bundle CASCADE", nativeQuery = true)
    void clearAllCosmeticBundles();

    boolean existsByExternalId(String externalId);

    List<Cosmetic> findAllByExternalIdIn(List<String> externalIds);

}
