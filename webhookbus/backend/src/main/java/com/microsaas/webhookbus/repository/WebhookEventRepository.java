package com.microsaas.webhookbus.repository;

import com.microsaas.webhookbus.entity.WebhookEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WebhookEventRepository extends JpaRepository<WebhookEvent, UUID> {
    Page<WebhookEvent> findByTenantId(UUID tenantId, Pageable pageable);
    Optional<WebhookEvent> findByIdAndTenantId(UUID id, UUID tenantId);
}
