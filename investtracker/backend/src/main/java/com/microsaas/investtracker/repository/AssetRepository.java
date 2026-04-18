package com.microsaas.investtracker.repository;

import com.microsaas.investtracker.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    Optional<Asset> findByTickerSymbolAndTenantId(String tickerSymbol, UUID tenantId);
    List<Asset> findByTenantId(UUID tenantId);
}
