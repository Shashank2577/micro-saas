package com.microsaas.peopleanalytics.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "engagement_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngagementScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Double score;

    private String source;

    @CreationTimestamp
    @Column(name = "calculated_at", updatable = false)
    private OffsetDateTime calculatedAt;
}
