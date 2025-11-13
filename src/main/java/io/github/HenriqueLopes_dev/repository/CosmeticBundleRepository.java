package io.github.HenriqueLopes_dev.repository;

import io.github.HenriqueLopes_dev.model.CosmeticBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CosmeticBundleRepository extends JpaRepository<CosmeticBundle, UUID>, JpaSpecificationExecutor<CosmeticBundle> {
}
