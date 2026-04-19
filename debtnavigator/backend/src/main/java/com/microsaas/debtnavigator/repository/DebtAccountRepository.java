package com.microsaas.debtnavigator.repository;

import com.microsaas.debtnavigator.entity.DebtAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DebtAccountRepository extends JpaRepository<DebtAccount, UUID> {
    List<DebtAccount> findByTenantId(UUID tenantId);
    Optional<DebtAccount> findByIdAndTenantId(UUID id, UUID tenantId);
}
