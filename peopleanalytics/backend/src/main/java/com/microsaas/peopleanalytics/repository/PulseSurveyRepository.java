package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.PulseSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface PulseSurveyRepository extends JpaRepository<PulseSurvey, UUID> {
    List<PulseSurvey> findByTenantId(UUID tenantId);
}
