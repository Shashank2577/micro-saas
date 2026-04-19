package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.AttributionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttributionModelRepository extends JpaRepository<AttributionModel, UUID> {
    List<AttributionModel> findByTenantId(UUID tenantId);
    Optional<AttributionModel> findByIdAndTenantId(UUID id, UUID tenantId);
}
