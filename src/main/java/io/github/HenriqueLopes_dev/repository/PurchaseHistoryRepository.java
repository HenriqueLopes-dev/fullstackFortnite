package io.github.HenriqueLopes_dev.repository;

import io.github.HenriqueLopes_dev.model.PurchaseHistory;
import io.github.HenriqueLopes_dev.model.Userr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, UUID>, JpaSpecificationExecutor<PurchaseHistory> {
    Page<PurchaseHistory> findByUser(Userr user, Pageable pageable);
    Page<PurchaseHistory> findByUserAndRefundFalse(Userr user, Pageable pageable);

    Optional<PurchaseHistory> findByIdAndUser(UUID id, Userr user);
}
