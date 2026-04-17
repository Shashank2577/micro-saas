package com.microsaas.apigatekeeper.repository;

import com.microsaas.apigatekeeper.entity.WebhookDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WebhookDeliveryRepository extends JpaRepository<WebhookDelivery, UUID> {
    List<WebhookDelivery> findByWebhookIdAndTenantId(UUID webhookId, String tenantId);
}
