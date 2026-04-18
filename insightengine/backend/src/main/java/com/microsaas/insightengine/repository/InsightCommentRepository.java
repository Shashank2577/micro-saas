package com.microsaas.insightengine.repository;

import com.microsaas.insightengine.entity.InsightComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsightCommentRepository extends JpaRepository<InsightComment, UUID> {
    List<InsightComment> findByInsightIdAndTenantIdOrderByCreatedAtAsc(UUID insightId, UUID tenantId);
}
