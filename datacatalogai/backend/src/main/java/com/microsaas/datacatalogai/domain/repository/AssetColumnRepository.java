package com.microsaas.datacatalogai.domain.repository;

import com.microsaas.datacatalogai.domain.model.AssetColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetColumnRepository extends JpaRepository<AssetColumn, UUID> {
    List<AssetColumn> findAllByTenantIdAndAssetId(String tenantId, UUID assetId);
    Optional<AssetColumn> findByIdAndTenantId(UUID id, String tenantId);
}
