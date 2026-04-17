package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.PressMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PressMentionRepository extends JpaRepository<PressMention, UUID> {
    List<PressMention> findByCompetitorIdAndTenantIdOrderByPublishedAtDesc(UUID competitorId, UUID tenantId);
}
