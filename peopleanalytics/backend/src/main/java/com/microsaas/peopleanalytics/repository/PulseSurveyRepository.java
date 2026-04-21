package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.PulseSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PulseSurveyRepository extends JpaRepository<PulseSurvey, UUID> {
    List<PulseSurvey> findAllByTenantId(UUID tenantId);
}
