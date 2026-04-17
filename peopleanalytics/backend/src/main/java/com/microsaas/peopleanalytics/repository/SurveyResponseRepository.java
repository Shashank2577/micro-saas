package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, UUID> {
    List<SurveyResponse> findByTenantIdAndSurveyId(UUID tenantId, UUID surveyId);
}
