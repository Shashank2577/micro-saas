package com.microsaas.datalineagetracker.repository;

import com.microsaas.datalineagetracker.entity.DataLineageLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataLineageLinkRepository extends JpaRepository<DataLineageLink, UUID> {
    List<DataLineageLink> findAllByTenantIdAndSourceAssetId(UUID tenantId, UUID sourceAssetId);
    List<DataLineageLink> findAllByTenantIdAndTargetAssetId(UUID tenantId, UUID targetAssetId);
    List<DataLineageLink> findAllByTenantId(UUID tenantId);
}
