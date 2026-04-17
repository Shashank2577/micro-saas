package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.Battlecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BattlecardRepository extends JpaRepository<Battlecard, UUID> {
    Optional<Battlecard> findTopByCompetitorIdAndTenantIdOrderByGeneratedAtDesc(UUID competitorId, UUID tenantId);
}
