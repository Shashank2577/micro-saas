package com.microsaas.callintelligence.domain.call;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CallTranscriptRepository extends JpaRepository<CallTranscript, UUID> {
    List<CallTranscript> findByCallIdAndTenantIdOrderByStartTimeAsc(UUID callId, UUID tenantId);
}
