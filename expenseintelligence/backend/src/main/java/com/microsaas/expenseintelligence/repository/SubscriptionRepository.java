package com.microsaas.expenseintelligence.repository;

import com.microsaas.expenseintelligence.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByTenantId(UUID tenantId);
    List<Subscription> findByTenantIdAndVendor(UUID tenantId, String vendor);
}
