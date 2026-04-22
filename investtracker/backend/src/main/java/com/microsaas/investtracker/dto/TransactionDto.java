package com.microsaas.investtracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class TransactionDto {
    private UUID id;
    private UUID accountId;
    private UUID assetId;
    private String type;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private OffsetDateTime transactionDate;
}