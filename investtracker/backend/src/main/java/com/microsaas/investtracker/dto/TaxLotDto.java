package com.microsaas.investtracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TaxLotDto {
    private UUID id;
    private UUID holdingId;
    private LocalDate purchaseDate;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;
}