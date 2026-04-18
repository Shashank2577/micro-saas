package com.tenantmanager.repository;

import com.tenantmanager.domain.TenantEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantEventRepository extends JpaRepository<TenantEvent, UUID> {
    List<TenantEvent> findByCustomerTenantIdAndTenantIdOrderByOccurredAtDesc(UUID customerTenantId, UUID tenantId);
}
