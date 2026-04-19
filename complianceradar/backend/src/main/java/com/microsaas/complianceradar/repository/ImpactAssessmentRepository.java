package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.ImpactAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImpactAssessmentRepository extends JpaRepository<ImpactAssessment, UUID> {
    List<ImpactAssessment> findAllByTenantId(UUID tenantId);
    Optional<ImpactAssessment> findByIdAndTenantId(UUID id, UUID tenantId);
    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
