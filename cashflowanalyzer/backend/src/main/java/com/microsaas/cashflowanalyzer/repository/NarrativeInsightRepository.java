package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.NarrativeInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NarrativeInsightRepository extends JpaRepository<NarrativeInsight, UUID> {
    List<NarrativeInsight> findByTenantId(UUID tenantId);
    Optional<NarrativeInsight> findByIdAndTenantId(UUID id, UUID tenantId);
}
