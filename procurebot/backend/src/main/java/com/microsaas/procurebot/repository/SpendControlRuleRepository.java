package com.microsaas.procurebot.repository;

import com.microsaas.procurebot.model.SpendControlRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpendControlRuleRepository extends JpaRepository<SpendControlRule, UUID> {
    List<SpendControlRule> findByTenantId(UUID tenantId);
    Optional<SpendControlRule> findByIdAndTenantId(UUID id, UUID tenantId);
}
