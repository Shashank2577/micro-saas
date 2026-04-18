package com.microsaas.billingsync.repository;

import com.microsaas.billingsync.model.MeterEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MeterEventRepository extends JpaRepository<MeterEvent, UUID> {
    List<MeterEvent> findByTenantId(String tenantId);
    
    @Query("SELECT SUM(m.quantity) FROM MeterEvent m WHERE m.tenantId = :tenantId AND m.subscription.id = :subscriptionId AND m.metricName = :metricName AND m.eventTimestamp >= :startDate AND m.eventTimestamp <= :endDate")
    Integer sumQuantityBySubscriptionAndMetricAndPeriod(@Param("tenantId") String tenantId, @Param("subscriptionId") UUID subscriptionId, @Param("metricName") String metricName, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
