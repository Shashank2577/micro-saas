package com.microsaas.debtnavigator.repository;

import com.microsaas.debtnavigator.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DebtRepository extends JpaRepository<Debt, UUID> {
    List<Debt> findByTenantId(String tenantId);
}
