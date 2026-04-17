package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, UUID> {
    List<AlertRule> findByTenantId(String tenantId);
    Optional<AlertRule> findByIdAndTenantId(UUID id, String tenantId);
}
