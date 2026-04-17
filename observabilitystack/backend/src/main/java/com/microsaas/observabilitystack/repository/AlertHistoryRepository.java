package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, UUID> {
    List<AlertHistory> findByTenantId(String tenantId);
    Optional<AlertHistory> findByIdAndTenantId(UUID id, String tenantId);
}
