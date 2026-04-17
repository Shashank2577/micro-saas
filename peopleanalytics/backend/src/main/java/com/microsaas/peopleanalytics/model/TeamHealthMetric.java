package com.microsaas.peopleanalytics.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "team_health_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamHealthMetric {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "health_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal healthScore;

    @Column(name = "collaboration_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal collaborationScore;

    @Column(name = "productivity_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal productivityScore;

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
