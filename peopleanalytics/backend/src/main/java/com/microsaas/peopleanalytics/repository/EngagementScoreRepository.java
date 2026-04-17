package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.EngagementScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

public interface EngagementScoreRepository extends JpaRepository<EngagementScore, UUID> {
    List<EngagementScore> findByTenantId(UUID tenantId);
    List<EngagementScore> findByTenantIdAndCalculatedAtAfter(UUID tenantId, LocalDateTime date);
}
