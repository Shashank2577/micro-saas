package com.microsaas.ndaflow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ndas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nda {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String counterparty;

    @Enumerated(EnumType.STRING)
    @Column(name = "nda_type", nullable = false)
    private NdaType ndaType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NdaStatus status;

    @Column(name = "expires_at")
    private LocalDate expiresAt;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
