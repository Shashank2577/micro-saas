package com.microsaas.callintelligence.domain.insight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SentimentAnalysisRepository extends JpaRepository<SentimentAnalysis, UUID> {
    List<SentimentAnalysis> findByCallIdAndTenantId(UUID callId, UUID tenantId);
}
