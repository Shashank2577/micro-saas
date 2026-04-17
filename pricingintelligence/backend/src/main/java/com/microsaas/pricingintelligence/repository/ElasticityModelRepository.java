package com.microsaas.pricingintelligence.repository;

import com.microsaas.pricingintelligence.domain.ElasticityModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface ElasticityModelRepository extends JpaRepository<ElasticityModel, UUID> {
    List<ElasticityModel> findByTenantId(UUID tenantId);
    Optional<ElasticityModel> findByTenantIdAndSegment(UUID tenantId, String segment);
}
