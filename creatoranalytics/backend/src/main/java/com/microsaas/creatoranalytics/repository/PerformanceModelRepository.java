package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.PerformanceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PerformanceModelRepository extends JpaRepository<PerformanceModel, UUID> {
    List<PerformanceModel> findByTenantId(UUID tenantId);
}
