package com.microsaas.investtracker.dto;

import lombok.Data;

@Data
public class CreatePortfolioRequest {
    private String name;
    private String currency;
    private String targetAllocation;
}
