package com.microsaas.queryoptimizer.repository;

import com.microsaas.queryoptimizer.domain.IndexSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IndexSuggestionRepository extends JpaRepository<IndexSuggestion, UUID> {
    List<IndexSuggestion> findByTenantId(UUID tenantId);
    List<IndexSuggestion> findByTenantIdAndFingerprintId(UUID tenantId, UUID fingerprintId);
}
