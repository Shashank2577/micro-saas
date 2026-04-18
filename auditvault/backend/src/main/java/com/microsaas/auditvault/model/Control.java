package com.microsaas.auditvault.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "control")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Control {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "framework_id", nullable = false)
    private UUID frameworkId;

    @Column(name = "control_code", nullable = false)
    private String controlCode;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "evidence_requirements")
    private String evidenceRequirements;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
