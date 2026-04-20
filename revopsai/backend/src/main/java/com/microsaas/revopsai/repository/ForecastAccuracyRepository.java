package com.microsaas.revopsai.repository;

import com.microsaas.revopsai.model.ForecastAccuracy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ForecastAccuracyRepository extends JpaRepository<ForecastAccuracy, UUID> {
    List<ForecastAccuracy> findByTenantId(UUID tenantId);
}
