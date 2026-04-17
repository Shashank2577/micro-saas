package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByTenantId(String tenantId);
    Optional<Alert> findByIdAndTenantId(UUID id, String tenantId);
}
