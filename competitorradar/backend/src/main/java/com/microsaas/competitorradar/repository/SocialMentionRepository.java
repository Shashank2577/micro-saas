package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.SocialMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SocialMentionRepository extends JpaRepository<SocialMention, UUID> {
    List<SocialMention> findByCompetitorIdAndTenantIdOrderByPostedAtDesc(UUID competitorId, UUID tenantId);
}
