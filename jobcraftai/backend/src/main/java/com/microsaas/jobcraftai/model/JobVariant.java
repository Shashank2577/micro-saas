package com.microsaas.jobcraftai.model;

import com.crosscutting.tenancy.TenantListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "job_variant")
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Builder.Default
    private Integer views = 0;

    @Builder.Default
    private Integer applications = 0;

    @Column(name = "quality_score")
    private BigDecimal qualityScore;
}
