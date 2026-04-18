package com.microsaas.investtracker.repository;

import com.microsaas.investtracker.entity.BrokerageAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrokerageAccountRepository extends JpaRepository<BrokerageAccount, UUID> {
    List<BrokerageAccount> findByTenantIdAndPortfolioId(UUID tenantId, UUID portfolioId);
    Optional<BrokerageAccount> findByIdAndTenantId(UUID id, UUID tenantId);
}
