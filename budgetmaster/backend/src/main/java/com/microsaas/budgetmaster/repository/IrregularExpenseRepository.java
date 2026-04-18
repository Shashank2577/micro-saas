package com.microsaas.budgetmaster.repository;

import com.microsaas.budgetmaster.domain.IrregularExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface IrregularExpenseRepository extends JpaRepository<IrregularExpense, UUID> {
    List<IrregularExpense> findAllByTenantId(UUID tenantId);
    Optional<IrregularExpense> findByIdAndTenantId(UUID id, UUID tenantId);
}
