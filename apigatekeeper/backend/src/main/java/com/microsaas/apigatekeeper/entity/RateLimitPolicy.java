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
@Table(name = "rate_limit_policies")
@Data
public class RateLimitPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String tenantId;
    private String name;
    private Integer replenishRate;
    private Integer burstCapacity;
    private Integer requestedTokens;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
