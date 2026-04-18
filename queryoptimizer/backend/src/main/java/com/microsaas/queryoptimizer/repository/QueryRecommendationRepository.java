package com.microsaas.queryoptimizer.repository;

import com.microsaas.queryoptimizer.domain.QueryRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QueryRecommendationRepository extends JpaRepository<QueryRecommendation, UUID> {
    List<QueryRecommendation> findByTenantId(UUID tenantId);
    List<QueryRecommendation> findByTenantIdAndFingerprintId(UUID tenantId, UUID fingerprintId);
    Optional<QueryRecommendation> findByIdAndTenantId(UUID id, UUID tenantId);
}
