package com.microsaas.supportintelligence.repository;

import com.microsaas.supportintelligence.model.ResponseSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResponseSuggestionRepository extends JpaRepository<ResponseSuggestion, UUID> {
    List<ResponseSuggestion> findByTenantId(UUID tenantId);
    Optional<ResponseSuggestion> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ResponseSuggestion> findByTicketIdAndTenantId(UUID ticketId, UUID tenantId);
}
