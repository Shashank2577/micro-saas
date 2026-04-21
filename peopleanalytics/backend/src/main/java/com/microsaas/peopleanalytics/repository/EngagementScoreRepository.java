package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.EngagementScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EngagementScoreRepository extends JpaRepository<EngagementScore, UUID> {
    List<EngagementScore> findAllByTenantId(UUID tenantId);
    List<EngagementScore> findAllByEmployeeIdAndTenantId(UUID employeeId, UUID tenantId);
}
