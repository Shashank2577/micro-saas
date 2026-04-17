package com.microsaas.datalineagetracker.repository;

import com.microsaas.datalineagetracker.entity.DataAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataAssetRepository extends JpaRepository<DataAsset, UUID> {
    List<DataAsset> findAllByTenantId(UUID tenantId);
    Optional<DataAsset> findByIdAndTenantId(UUID id, UUID tenantId);
}
