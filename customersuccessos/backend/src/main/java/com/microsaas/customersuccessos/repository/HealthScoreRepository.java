package com.microsaas.customersuccessos.repository;

import com.microsaas.customersuccessos.model.HealthScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface HealthScoreRepository extends JpaRepository<HealthScore, UUID> {
    List<HealthScore> findByTenantIdAndAccountIdOrderByRecordedAtDesc(UUID tenantId, UUID accountId);
}
