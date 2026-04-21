package com.microsaas.peopleanalytics.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "team_health_metrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamHealthMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    private String department;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "collaboration_score")
    private Double collaborationScore;

    @Column(name = "productivity_signal")
    private Double productivitySignal;

    @CreationTimestamp
    @Column(name = "measured_at", updatable = false)
    private OffsetDateTime measuredAt;
}
