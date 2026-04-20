package com.microsaas.contextlayer.domain;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "interaction_histories", indexes = {
    @Index(name = "idx_customer_app_timestamp", columnList = "customer_id, app_id, timestamp")
})
public class InteractionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "interaction_id")
    private UUID interactionId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "app_id", nullable = false)
    private String appId;

    @Column(name = "interaction_type", nullable = false)
    private String interactionType;

    @Column(name = "timestamp")
    private Instant timestamp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "outcomes")
    private String outcomes;

    public UUID getInteractionId() {
        return this.interactionId;
    }
    public void setInteractionId(UUID interactionId) {
        this.interactionId = interactionId;
    }
    public String getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public UUID getTenantId() {
        return this.tenantId;
    }
    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }
    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getInteractionType() {
        return this.interactionType;
    }
    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }
    public Instant getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public String getMetadata() {
        return this.metadata;
    }
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    public String getOutcomes() {
        return this.outcomes;
    }
    public void setOutcomes(String outcomes) {
        this.outcomes = outcomes;
    }
}
