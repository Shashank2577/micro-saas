package com.microsaas.datalineagetracker.repository;

import com.microsaas.datalineagetracker.entity.PiiTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PiiTagRepository extends JpaRepository<PiiTag, UUID> {
    List<PiiTag> findAllByTenantIdAndAssetId(UUID tenantId, UUID assetId);
}
