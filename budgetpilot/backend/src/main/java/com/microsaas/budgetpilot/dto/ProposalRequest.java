package com.microsaas.budgetpilot.dto;

import lombok.Data;

@Data
public class ProposalRequest {
    private String department;
    private Integer fiscalYear;
    private String goals;
    private String historicalData;
}
