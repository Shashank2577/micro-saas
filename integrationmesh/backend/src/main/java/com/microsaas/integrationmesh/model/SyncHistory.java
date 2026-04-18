package com.microsaas.integrationmesh.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "sync_history")
@Data
public class SyncHistory {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integration_id")
    private Integration integration;

    private String status;

    @Column(name = "records_processed")
    private Integer recordsProcessed = 0;

    @Column(name = "records_failed")
    private Integer recordsFailed = 0;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
