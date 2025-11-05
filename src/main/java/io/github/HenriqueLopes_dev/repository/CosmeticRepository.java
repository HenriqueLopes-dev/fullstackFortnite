package io.github.HenriqueLopes_dev.repository;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;
import io.github.HenriqueLopes_dev.model.Cosmetic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CosmeticRepository extends JpaRepository<Cosmetic, UUID>, JpaSpecificationExecutor<Cosmetic> {

    Optional<Cosmetic> findByExternalId(String externalId);

    @Modifying
    @Query("UPDATE Cosmetic c SET c.onShop = false AND c.isNew = false AND c.regularPrice = null AND c.finalPrice = null")
    void updateAllByDefault();

    boolean existsByExternalId(String externalId);

    List<Cosmetic> findAllByExternalId(List<String> externalIds);

}
