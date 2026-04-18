package com.microsaas.insightengine.repository;

import com.microsaas.insightengine.entity.MetricData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MetricDataRepository extends JpaRepository<MetricData, UUID> {
    List<MetricData> findByTenantIdAndTimestampAfter(UUID tenantId, LocalDateTime timestamp);
}
