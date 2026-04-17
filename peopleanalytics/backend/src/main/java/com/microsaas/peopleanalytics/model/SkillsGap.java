package com.microsaas.peopleanalytics.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "skills_gap")
@Data
public class SkillsGap {
    @Id
    private UUID id;
    private UUID tenantId;
    private String department;
    private String skillName;
    private BigDecimal currentCoverage;
    private BigDecimal requiredCoverage;
    private String gapSeverity;
    private LocalDateTime identifiedAt;
}
