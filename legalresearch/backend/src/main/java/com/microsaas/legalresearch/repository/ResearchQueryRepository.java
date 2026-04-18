package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.ResearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResearchQueryRepository extends JpaRepository<ResearchQuery, UUID> {
    List<ResearchQuery> findByTenantId(UUID tenantId);
    Optional<ResearchQuery> findByIdAndTenantId(UUID id, UUID tenantId);
}
