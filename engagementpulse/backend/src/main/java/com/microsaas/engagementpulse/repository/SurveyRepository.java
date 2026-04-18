package com.microsaas.engagementpulse.repository;

import com.microsaas.engagementpulse.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {
    List<Survey> findAllByTenantId(UUID tenantId);
}
