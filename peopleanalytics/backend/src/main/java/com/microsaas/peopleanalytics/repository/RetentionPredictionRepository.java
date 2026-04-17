package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.RetentionPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface RetentionPredictionRepository extends JpaRepository<RetentionPrediction, UUID> {
    List<RetentionPrediction> findByTenantId(UUID tenantId);
}
