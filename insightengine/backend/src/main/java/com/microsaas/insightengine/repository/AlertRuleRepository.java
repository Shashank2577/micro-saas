package com.microsaas.insightengine.repository;

import com.microsaas.insightengine.entity.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, UUID> {
    List<AlertRule> findByTenantId(UUID tenantId);
    Optional<AlertRule> findByIdAndTenantId(UUID id, UUID tenantId);
    List<AlertRule> findByTenantIdAndIsActiveTrue(UUID tenantId);
}
