package com.microsaas.integrationmesh.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "integrations")
@Data
public class Integration {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_connector_id")
    private Connector sourceConnector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_connector_id")
    private Connector targetConnector;

    private String status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private ZonedDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
