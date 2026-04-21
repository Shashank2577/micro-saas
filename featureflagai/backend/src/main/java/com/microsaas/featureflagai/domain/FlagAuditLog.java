package com.microsaas.featureflagai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "flag_audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlagAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "flag_id", nullable = false)
    private UUID flagId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = OffsetDateTime.now();
        }
    }
}
