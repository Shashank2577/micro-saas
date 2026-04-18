



package com.crosscutting.socialintelligence.domain;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "audience_demographics")
public class AudienceDemographic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private PlatformAccount account;

    @Column(name = "demographic_type", nullable = false)
    private String demographicType;

    @Column(name = "demographic_value", nullable = false)
    private String demographicValue;

    @Column(name = "percentage", nullable = false)
    private BigDecimal percentage;

    @Column(name = "recorded_at", insertable = false, updatable = false)
    private ZonedDateTime recordedAt;
}
