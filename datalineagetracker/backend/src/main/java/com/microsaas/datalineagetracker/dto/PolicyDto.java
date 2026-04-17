package com.microsaas.datalineagetracker.dto;

import lombok.Data;

@Data
public class PolicyDto {
    private String name;
    private String description;
    private String policyType;
    private String rules; // json string
    private Boolean isActive;
}
