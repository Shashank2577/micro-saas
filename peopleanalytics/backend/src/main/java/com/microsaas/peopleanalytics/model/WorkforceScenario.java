package com.microsaas.peopleanalytics.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.Map;

@Entity
@Table(name = "workforce_scenario")
@Data
public class WorkforceScenario {
    @Id
    private UUID id;
    private UUID tenantId;
    private String name;
    private int baselineHeadcount;
    private int projectedHeadcount;
    private BigDecimal burnRateMonthly;
    private BigDecimal runwayMonths;
    
    @Type(JsonBinaryType.class)
    private Map<String, Object> assumptions;
}
