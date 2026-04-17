package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, UUID> {
    List<Competitor> findByTenantId(UUID tenantId);
    Optional<Competitor> findByIdAndTenantId(UUID id, UUID tenantId);
}
