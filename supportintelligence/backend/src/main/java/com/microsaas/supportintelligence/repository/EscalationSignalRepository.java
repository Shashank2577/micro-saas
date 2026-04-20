package com.microsaas.supportintelligence.repository;

import com.microsaas.supportintelligence.model.EscalationSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EscalationSignalRepository extends JpaRepository<EscalationSignal, UUID> {
    List<EscalationSignal> findByTenantId(UUID tenantId);
    Optional<EscalationSignal> findByIdAndTenantId(UUID id, UUID tenantId);
    List<EscalationSignal> findByTicketIdAndTenantId(UUID ticketId, UUID tenantId);
}
