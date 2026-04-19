package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.CompBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompBandRepository extends JpaRepository<CompBand, UUID> {
    List<CompBand> findByTenantId(UUID tenantId);
    Optional<CompBand> findByIdAndTenantId(UUID id, UUID tenantId);
}
