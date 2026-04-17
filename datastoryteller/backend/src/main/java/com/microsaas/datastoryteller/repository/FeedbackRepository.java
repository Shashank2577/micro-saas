package com.microsaas.datastoryteller.repository;

import com.microsaas.datastoryteller.domain.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByReportIdAndTenantId(UUID reportId, String tenantId);
}
