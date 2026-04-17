package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.Runbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RunbookRepository extends JpaRepository<Runbook, UUID> {
    List<Runbook> findByTenantId(String tenantId);
    Optional<Runbook> findByIdAndTenantId(UUID id, String tenantId);
}
