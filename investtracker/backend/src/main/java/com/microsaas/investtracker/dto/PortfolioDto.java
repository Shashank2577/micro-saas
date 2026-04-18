package com.microsaas.investtracker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PortfolioDto {
    private UUID id;
    private String name;
    private String currency;
    private String targetAllocation;
}
