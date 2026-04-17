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

import java.util.UUID;

@Entity
@Table(name = "bias_flag")
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BiasFlag {

    public enum BiasType { GENDER, AGE, CULTURAL }
    public enum Severity { LOW, MEDIUM, HIGH }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @Column(columnDefinition = "text", nullable = false)
    private String phrase;

    @Enumerated(EnumType.STRING)
    @Column(name = "bias_type", nullable = false)
    private BiasType biasType;

    @Column(columnDefinition = "text")
    private String suggestion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;
}
