package com.microsaas.contextlayer.repository;

import com.microsaas.contextlayer.domain.CustomerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerPreferenceRepository extends JpaRepository<CustomerPreference, UUID> {
    List<CustomerPreference> findByCustomerIdAndTenantId(String customerId, UUID tenantId);
    Optional<CustomerPreference> findByCustomerIdAndTenantIdAndPreferenceKey(String customerId, UUID tenantId, String preferenceKey);
}
