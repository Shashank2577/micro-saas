package com.microsaas.contractportfolio.repository;

import com.microsaas.contractportfolio.domain.ClauseExtraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClauseExtractionRepository extends JpaRepository<ClauseExtraction, UUID> {
    List<ClauseExtraction> findAllByTenantId(UUID tenantId);
    Optional<ClauseExtraction> findByIdAndTenantId(UUID id, UUID tenantId);
}
