package com.microsaas.callintelligence.domain.insight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CallInsightRepository extends JpaRepository<CallInsight, UUID> {
    List<CallInsight> findByCallIdAndTenantId(UUID callId, UUID tenantId);
}
