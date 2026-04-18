package com.microsaas.wealthedge.repository;

import com.microsaas.wealthedge.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    List<Portfolio> findAllByTenantId(UUID tenantId);
    Optional<Portfolio> findByIdAndTenantId(UUID id, UUID tenantId);
}
