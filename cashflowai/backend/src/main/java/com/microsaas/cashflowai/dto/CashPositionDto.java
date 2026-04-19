package com.microsaas.cashflowai.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CashPositionDto {
    private UUID id;
    private String name;
    private String status;
    private String metadataJson;
    private LocalDate asOf;
    private BigDecimal availableCash;
    private BigDecimal restrictedCash;
}
