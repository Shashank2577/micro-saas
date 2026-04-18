package com.microsaas.engagementpulse.repository;

import com.microsaas.engagementpulse.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, UUID> {
    List<SurveyResponse> findByTeamIdAndTenantId(UUID teamId, UUID tenantId);
    List<SurveyResponse> findAllByTenantIdAndSubmittedAtBetween(UUID tenantId, LocalDateTime start, LocalDateTime end);
}
