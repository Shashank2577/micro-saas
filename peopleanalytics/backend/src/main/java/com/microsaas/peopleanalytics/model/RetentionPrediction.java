package com.microsaas.peopleanalytics.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "retention_predictions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetentionPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "risk_score")
    private Double riskScore;

    @Column(name = "risk_level")
    private String riskLevel;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> factors;

    @CreationTimestamp
    @Column(name = "predicted_at", updatable = false)
    private OffsetDateTime predictedAt;
}
