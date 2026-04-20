package com.microsaas.pricingintelligence.repository;

import com.microsaas.pricingintelligence.domain.ElasticityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElasticityModelRepository extends JpaRepository<ElasticityModel, UUID> {
    List<ElasticityModel> findByTenantId(UUID tenantId);
    Optional<ElasticityModel> findByTenantIdAndSegmentId(UUID tenantId, UUID segmentId);
}
