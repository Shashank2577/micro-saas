package com.microsaas.dataroomai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "diligence_gaps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiligenceGap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private DataRoom room;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, name = "missing_item", columnDefinition = "TEXT")
    private String missingItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Importance importance;

    @Column(nullable = false, name = "tenant_id")
    private UUID tenantId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
