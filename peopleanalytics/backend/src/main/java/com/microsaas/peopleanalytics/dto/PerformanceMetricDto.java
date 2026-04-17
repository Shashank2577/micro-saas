package com.microsaas.peopleanalytics.dto;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
public class PerformanceMetricDto {
    private UUID employeeId;
    private String metricType;
    private String metricName;
    private BigDecimal metricValue;
    private LocalDate metricDate;
    private String notes;
}
