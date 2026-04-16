package com.microsaas.expenseintelligence.repository;

import com.microsaas.expenseintelligence.model.PolicyViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyViolationRepository extends JpaRepository<PolicyViolation, UUID> {
    List<PolicyViolation> findByTenantId(UUID tenantId);
    List<PolicyViolation> findByExpenseId(UUID expenseId);
}
