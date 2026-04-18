package com.microsaas.webhookbus.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "webhook_deliveries")
public class WebhookDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private WebhookEvent event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private WebhookEndpoint endpoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "attempt_count")
    private Integer attemptCount = 0;

    @Column(name = "last_attempt_at")
    private ZonedDateTime lastAttemptAt;

    @Column(name = "next_attempt_at")
    private ZonedDateTime nextAttemptAt;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
    
    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public WebhookEvent getEvent() { return event; }
    public void setEvent(WebhookEvent event) { this.event = event; }
    public WebhookEndpoint getEndpoint() { return endpoint; }
    public void setEndpoint(WebhookEndpoint endpoint) { this.endpoint = endpoint; }
    public DeliveryStatus getStatus() { return status; }
    public void setStatus(DeliveryStatus status) { this.status = status; }
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public Integer getAttemptCount() { return attemptCount; }
    public void setAttemptCount(Integer attemptCount) { this.attemptCount = attemptCount; }
    public ZonedDateTime getLastAttemptAt() { return lastAttemptAt; }
    public void setLastAttemptAt(ZonedDateTime lastAttemptAt) { this.lastAttemptAt = lastAttemptAt; }
    public ZonedDateTime getNextAttemptAt() { return nextAttemptAt; }
    public void setNextAttemptAt(ZonedDateTime nextAttemptAt) { this.nextAttemptAt = nextAttemptAt; }
    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}
