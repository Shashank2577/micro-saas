package com.microsaas.auditvault.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "evidence_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "control_id", nullable = false)
    private UUID controlId;

    @Column(name = "requested_by", nullable = false)
    private String requestedBy;

    @Column(nullable = false)
    private String status;

    @Column(name = "due_date")
    private OffsetDateTime dueDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
