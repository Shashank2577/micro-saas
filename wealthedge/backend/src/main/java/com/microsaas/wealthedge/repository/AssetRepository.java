package com.microsaas.wealthedge.repository;

import com.microsaas.wealthedge.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findAllByTenantId(UUID tenantId);
    Optional<Asset> findByIdAndTenantId(UUID id, UUID tenantId);
}
