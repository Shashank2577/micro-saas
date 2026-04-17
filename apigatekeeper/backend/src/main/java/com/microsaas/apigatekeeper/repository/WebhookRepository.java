package com.microsaas.apigatekeeper.repository;

import com.microsaas.apigatekeeper.entity.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WebhookRepository extends JpaRepository<Webhook, UUID> {
    List<Webhook> findByTenantId(String tenantId);
    Optional<Webhook> findByIdAndTenantId(UUID id, String tenantId);
}
