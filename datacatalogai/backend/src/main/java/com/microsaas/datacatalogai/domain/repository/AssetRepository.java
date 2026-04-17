package com.microsaas.datacatalogai.domain.repository;

import com.microsaas.datacatalogai.domain.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    Page<Asset> findAllByTenantId(String tenantId, Pageable pageable);
    List<Asset> findAllByTenantId(String tenantId);
    Optional<Asset> findByIdAndTenantId(UUID id, String tenantId);
    List<Asset> findAllByTenantIdAndSourceId(String tenantId, UUID sourceId);
}
