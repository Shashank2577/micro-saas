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
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.json.JsonType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;

@Entity
@Table(name = "job_description")
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDescription {

    public enum Status { DRAFT, ACTIVE, ARCHIVED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String title;

    private String department;

    @Column(name = "role_level")
    private String roleLevel;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> requirements;

    @Column(name = "generated_content", columnDefinition = "text")
    private String generatedContent;

    @Column(name = "bias_score")
    private BigDecimal biasScore;

    @Column(name = "seo_score")
    private BigDecimal seoScore;

    @Builder.Default
    private Integer version = 1;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.DRAFT;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
