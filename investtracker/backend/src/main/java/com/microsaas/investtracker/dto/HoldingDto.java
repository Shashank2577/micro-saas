package com.microsaas.investtracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class HoldingDto {
    private UUID id;
    private UUID accountId;
    private UUID assetId;
    private BigDecimal quantity;
    private BigDecimal averageCostBasis;
}