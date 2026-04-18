package com.microsaas.insightengine.repository;

import com.microsaas.insightengine.entity.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InsightRepository extends JpaRepository<Insight, UUID> {
    List<Insight> findByTenantIdOrderByImpactScoreDesc(UUID tenantId);
    Optional<Insight> findByIdAndTenantId(UUID id, UUID tenantId);
}
