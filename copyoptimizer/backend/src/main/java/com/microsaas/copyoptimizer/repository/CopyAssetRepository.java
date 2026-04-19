package com.microsaas.copyoptimizer.repository;

import com.microsaas.copyoptimizer.model.CopyAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CopyAssetRepository extends JpaRepository<CopyAsset, UUID> {
    List<CopyAsset> findByTenantId(UUID tenantId);
    Optional<CopyAsset> findByIdAndTenantId(UUID id, UUID tenantId);
}
