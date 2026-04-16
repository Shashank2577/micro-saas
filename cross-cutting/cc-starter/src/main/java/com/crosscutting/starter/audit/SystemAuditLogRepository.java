package com.crosscutting.starter.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemAuditLogRepository extends JpaRepository<SystemAuditLog, Long> {

    Page<SystemAuditLog> findByTenantId(UUID tenantId, Pageable pageable);

    @Query("SELECT s FROM SystemAuditLog s WHERE s.tenantId = :tenantId OR s.tenantId IS NULL")
    Page<SystemAuditLog> findByTenantIdIncludingNull(@Param("tenantId") UUID tenantId, Pageable pageable);

    Page<SystemAuditLog> findByUserId(UUID userId, Pageable pageable);

    List<SystemAuditLog> findByCorrelationId(UUID correlationId);

    Page<SystemAuditLog> findByEventType(String eventType, Pageable pageable);

    Page<SystemAuditLog> findByTenantIdAndEventType(UUID tenantId, String eventType, Pageable pageable);

    Page<SystemAuditLog> findByResourceTypeAndResourceId(String resourceType, String resourceId, Pageable pageable);
}
