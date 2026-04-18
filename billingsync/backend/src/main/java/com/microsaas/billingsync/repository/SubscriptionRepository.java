package com.microsaas.billingsync.repository;

import com.microsaas.billingsync.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByTenantId(String tenantId);
    Optional<Subscription> findByIdAndTenantId(UUID id, String tenantId);
}
