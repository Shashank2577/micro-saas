package com.microsaas.contextlayer.repository;

import com.microsaas.contextlayer.domain.CustomerContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerContextRepository extends JpaRepository<CustomerContext, UUID> {
    Optional<CustomerContext> findByCustomerIdAndTenantId(String customerId, UUID tenantId);
    void deleteByCustomerIdAndTenantId(String customerId, UUID tenantId);
}
