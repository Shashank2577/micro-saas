package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.ContentAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentAssetRepository extends JpaRepository<ContentAsset, UUID> {
    List<ContentAsset> findByTenantId(UUID tenantId);
    Optional<ContentAsset> findByIdAndTenantId(UUID id, UUID tenantId);
}
