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
@Table(name = "webhooks")
@Data
public class Webhook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String tenantId;
    private String eventType;
    private String targetUrl;
    private String secret;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
