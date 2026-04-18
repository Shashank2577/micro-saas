package com.microsaas.datagovernanceos.repository;

import com.microsaas.datagovernanceos.entity.DataAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataAssetRepository extends JpaRepository<DataAsset, UUID> {
    List<DataAsset> findByTenantId(UUID tenantId);
    Optional<DataAsset> findByIdAndTenantId(UUID id, UUID tenantId);
}
