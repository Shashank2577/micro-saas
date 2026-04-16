package com.microsaas.onboardflow.model;

import com.crosscutting.tenancy.TenantListener;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "onboarding_plans")
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String department;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Type(JsonType.class)
    @Column(name = "plan_30_day", columnDefinition = "jsonb")
    private Map<String, Object> plan30Day;

    @Type(JsonType.class)
    @Column(name = "plan_60_day", columnDefinition = "jsonb")
    private Map<String, Object> plan60Day;

    @Type(JsonType.class)
    @Column(name = "plan_90_day", columnDefinition = "jsonb")
    private Map<String, Object> plan90Day;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public enum Status {
        DRAFT, ACTIVE, COMPLETED, CANCELLED
    }
}
