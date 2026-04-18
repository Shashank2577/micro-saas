package com.microsaas.budgetmaster.repository;

import com.microsaas.budgetmaster.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByBudgetIdAndTenantId(UUID budgetId, UUID tenantId);
    Optional<Category> findByIdAndTenantId(UUID id, UUID tenantId);
}
