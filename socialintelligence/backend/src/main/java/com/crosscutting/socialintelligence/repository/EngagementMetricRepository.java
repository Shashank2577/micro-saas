package com.crosscutting.socialintelligence.repository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.crosscutting.socialintelligence.domain.EngagementMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EngagementMetricRepository extends JpaRepository<EngagementMetric, UUID> {
    List<EngagementMetric> findByTenantIdAndMetricDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);
    List<EngagementMetric> findByTenantIdAndAccountPlatformNameAndMetricDateBetween(String tenantId, String platformName, LocalDate startDate, LocalDate endDate);
}
