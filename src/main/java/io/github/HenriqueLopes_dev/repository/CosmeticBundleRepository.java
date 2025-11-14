package io.github.HenriqueLopes_dev.repository;

import io.github.HenriqueLopes_dev.model.CosmeticBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CosmeticBundleRepository extends JpaRepository<CosmeticBundle, UUID>, JpaSpecificationExecutor<CosmeticBundle> {
    @Query("""
            SELECT DISTINCT cb FROM CosmeticBundle cb
            LEFT JOIN FETCH cb.cosmetics cbr
            LEFT JOIN FETCH cbr.cosmetic
            WHERE cb.id = :bundleId
            """)
    Optional<CosmeticBundle> findByIdWithCosmetics(@Param("bundleId") UUID bundleId);
}
