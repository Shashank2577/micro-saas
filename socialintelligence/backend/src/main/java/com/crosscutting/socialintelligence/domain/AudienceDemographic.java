package com.crosscutting.socialintelligence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audience_demographics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AudienceDemographic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private PlatformAccount account;
    private String ageRange;
    private String gender;
    private String country;
    private BigDecimal percentage;
    @Builder.Default
    private LocalDateTime recordedAt = LocalDateTime.now();
}
