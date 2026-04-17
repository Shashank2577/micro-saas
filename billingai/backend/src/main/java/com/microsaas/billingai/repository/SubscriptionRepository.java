package com.microsaas.billingai.repository;

import com.microsaas.billingai.model.Subscription;
import com.microsaas.billingai.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByTenantId(UUID tenantId);
    List<Subscription> findByCustomerIdAndTenantId(UUID customerId, UUID tenantId);
    List<Subscription> findByStatusAndTenantId(SubscriptionStatus status, UUID tenantId);
}
