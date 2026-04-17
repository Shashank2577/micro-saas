package com.microsaas.datacatalogai.domain.repository;

import com.microsaas.datacatalogai.domain.model.QueryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QueryLogRepository extends JpaRepository<QueryLog, UUID> {
    List<QueryLog> findAllByTenantIdAndAssetId(String tenantId, UUID assetId);
    Optional<QueryLog> findByIdAndTenantId(UUID id, String tenantId);
}
