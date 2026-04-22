package com.microsaas.compensationos.repository;

import com.microsaas.compensationos.entity.DepartmentBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentBudgetRepository extends JpaRepository<DepartmentBudget, UUID> {
    List<DepartmentBudget> findByTenantId(UUID tenantId);
}
