package com.microsaas.hiresignal.model;

import com.crosscutting.tenancy.TenantListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.UUID;

@Entity
@Table(name = "evaluations")
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "requisition_id", nullable = false)
    private UUID requisitionId;

    @Column(name = "evaluator_id", nullable = false)
    private UUID evaluatorId;

    @Column(columnDefinition = "TEXT")
    private String scorecard;

    @Column(name = "consistency_score")
    private Integer consistencyScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Recommendation recommendation;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    public enum Recommendation {
        STRONG_YES, YES, NO, STRONG_NO
    }
}
