package io.github.HenriqueLopes_dev.repository;

import io.github.HenriqueLopes_dev.model.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, UUID>, JpaSpecificationExecutor<PurchaseHistory> {
}
