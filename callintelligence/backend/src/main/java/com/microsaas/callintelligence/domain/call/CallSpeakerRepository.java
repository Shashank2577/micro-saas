package com.microsaas.callintelligence.domain.call;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CallSpeakerRepository extends JpaRepository<CallSpeaker, UUID> {
    List<CallSpeaker> findByCallIdAndTenantId(UUID callId, UUID tenantId);
}
