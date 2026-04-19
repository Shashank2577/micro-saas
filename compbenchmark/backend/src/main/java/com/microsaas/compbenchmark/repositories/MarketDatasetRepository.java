package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.MarketDataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarketDatasetRepository extends JpaRepository<MarketDataset, UUID> {
    List<MarketDataset> findByTenantId(UUID tenantId);
    Optional<MarketDataset> findByIdAndTenantId(UUID id, UUID tenantId);
}
