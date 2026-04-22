package com.microsaas.investtracker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountDto {
    private UUID id;
    private UUID portfolioId;
    private String brokerageName;
    private String accountNumber;
    private String syncStatus;
}