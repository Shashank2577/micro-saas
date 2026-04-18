package com.tenantmanager.repository;

import com.tenantmanager.domain.CustomerTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerTenantRepository extends JpaRepository<CustomerTenant, UUID> {
    List<CustomerTenant> findByTenantId(UUID tenantId);
    Optional<CustomerTenant> findByIdAndTenantId(UUID id, UUID tenantId);
}
