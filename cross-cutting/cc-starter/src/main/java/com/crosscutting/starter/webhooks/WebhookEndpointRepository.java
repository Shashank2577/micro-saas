package com.crosscutting.starter.webhooks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WebhookEndpointRepository extends JpaRepository<WebhookEndpoint, UUID> {

    List<WebhookEndpoint> findByTenantIdAndActiveTrue(UUID tenantId);

    /**
     * Find endpoints subscribed to a specific event type within a tenant.
     * The event_types column stores a comma-separated string, so we search
     * for an exact event type match using a native query that splits on commas
     * and trims whitespace, avoiding substring false-positives from LIKE.
     */
    @Query(value = "SELECT * FROM cc.webhook_endpoints " +
            "WHERE tenant_id = :tenantId " +
            "AND :eventType = ANY(string_to_array(event_types, ','))",
            nativeQuery = true)
    List<WebhookEndpoint> findByTenantIdAndEventType(
            @Param("tenantId") UUID tenantId,
            @Param("eventType") String eventType);
}
