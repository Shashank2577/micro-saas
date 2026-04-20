package com.microsaas.pricingintelligence.repository;

import com.microsaas.pricingintelligence.domain.ChurnAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChurnAnalysisRepository extends JpaRepository<ChurnAnalysis, UUID> {
    List<ChurnAnalysis> findByTenantId(UUID tenantId);
    List<ChurnAnalysis> findByTenantIdAndSegmentId(UUID tenantId, UUID segmentId);
}
