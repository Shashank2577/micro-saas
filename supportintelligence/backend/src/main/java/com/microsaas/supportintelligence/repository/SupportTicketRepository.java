package com.microsaas.supportintelligence.repository;

import com.microsaas.supportintelligence.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
    List<SupportTicket> findByTenantId(UUID tenantId);
    Optional<SupportTicket> findByIdAndTenantId(UUID id, UUID tenantId);
}
