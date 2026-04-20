package com.microsaas.supportintelligence.repository;

import com.microsaas.supportintelligence.model.TicketPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketPatternRepository extends JpaRepository<TicketPattern, UUID> {
    List<TicketPattern> findByTenantId(UUID tenantId);
    Optional<TicketPattern> findByIdAndTenantId(UUID id, UUID tenantId);
}
