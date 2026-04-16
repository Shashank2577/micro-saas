package com.crosscutting.starter.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessAuditLogRepository extends JpaRepository<BusinessAuditLog, Long> {

    Page<BusinessAuditLog> findByTenantId(UUID tenantId, Pageable pageable);

    @Query("SELECT b FROM BusinessAuditLog b WHERE b.tenantId = :tenantId OR b.tenantId IS NULL")
    Page<BusinessAuditLog> findByTenantIdIncludingNull(@Param("tenantId") UUID tenantId, Pageable pageable);

    Page<BusinessAuditLog> findByResourceTypeAndResourceId(String resourceType, UUID resourceId, Pageable pageable);

    Page<BusinessAuditLog> findByUserId(UUID userId, Pageable pageable);
}
