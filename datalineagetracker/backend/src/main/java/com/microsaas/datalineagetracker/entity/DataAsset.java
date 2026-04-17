package com.microsaas.datalineagetracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "data_assets")
@Data
public class DataAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // TABLE, VIEW, STREAM, REPORT, MODEL

    @Column(name = "source_system")
    private String sourceSystem;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "steward_id")
    private UUID stewardId;

    private String classification; // PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED
    
    private String description;

    @Column(name = "retention_days")
    private Integer retentionDays;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
