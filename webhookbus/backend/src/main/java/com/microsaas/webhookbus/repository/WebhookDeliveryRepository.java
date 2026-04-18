package com.microsaas.webhookbus.repository;

import com.microsaas.webhookbus.entity.DeliveryStatus;
import com.microsaas.webhookbus.entity.WebhookDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WebhookDeliveryRepository extends JpaRepository<WebhookDelivery, UUID> {
    Page<WebhookDelivery> findByTenantId(UUID tenantId, Pageable pageable);
    Page<WebhookDelivery> findByTenantIdAndEventId(UUID tenantId, UUID eventId, Pageable pageable);
    Page<WebhookDelivery> findByTenantIdAndEndpointId(UUID tenantId, UUID endpointId, Pageable pageable);
    Page<WebhookDelivery> findByTenantIdAndStatus(UUID tenantId, DeliveryStatus status, Pageable pageable);
    Optional<WebhookDelivery> findByIdAndTenantId(UUID id, UUID tenantId);
    List<WebhookDelivery> findByStatusAndNextAttemptAtBefore(DeliveryStatus status, ZonedDateTime time);
}
