package com.microsaas.callintelligence.domain.call;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CallParticipantRepository extends JpaRepository<CallParticipant, UUID> {
    List<CallParticipant> findByCallIdAndTenantId(UUID callId, UUID tenantId);
}
