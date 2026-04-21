package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, UUID> {
    List<SurveyResponse> findAllByTenantId(UUID tenantId);
    List<SurveyResponse> findAllBySurveyIdAndTenantId(UUID surveyId, UUID tenantId);
}
