package com.microsaas.billingai.repository;

import com.microsaas.billingai.model.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, UUID> {
    List<TaxRule> findByTenantId(UUID tenantId);
}
