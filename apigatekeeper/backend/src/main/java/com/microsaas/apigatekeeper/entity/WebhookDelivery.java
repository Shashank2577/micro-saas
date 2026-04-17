package com.microsaas.apigatekeeper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "webhook_deliveries")
@Data
public class WebhookDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String tenantId;
    private UUID webhookId;
    private String payload;
    private String status;
    private Integer retryCount;
    private ZonedDateTime nextRetryAt;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
