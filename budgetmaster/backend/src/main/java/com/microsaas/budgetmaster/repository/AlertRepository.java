package com.microsaas.budgetmaster.repository;

import com.microsaas.budgetmaster.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findAllByTenantId(UUID tenantId);
    List<Alert> findAllByCategoryIdAndTenantId(UUID categoryId, UUID tenantId);
    Optional<Alert> findByIdAndTenantId(UUID id, UUID tenantId);
}
