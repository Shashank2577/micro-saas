package com.microsaas.contextlayer.repository;

import com.microsaas.contextlayer.domain.ContextVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContextVersionRepository extends JpaRepository<ContextVersion, UUID> {
    List<ContextVersion> findByCustomerIdAndTenantIdOrderByCreatedAtDesc(String customerId, UUID tenantId);
}
