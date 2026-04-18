package com.microsaas.equityintelligence.repositories;

import com.microsaas.equityintelligence.model.Shareholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShareholderRepository extends JpaRepository<Shareholder, UUID> {
    List<Shareholder> findByTenantId(UUID tenantId);
    Optional<Shareholder> findByIdAndTenantId(UUID id, UUID tenantId);
}
