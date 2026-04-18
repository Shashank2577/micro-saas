package com.microsaas.insightengine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "custom_discovery_rules")
@Data
public class CustomDiscoveryRule {
    @Id
    private UUID id = UUID.randomUUID();
    private UUID tenantId;
    private String name;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String definition; // Using String to represent JSON
    
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}
