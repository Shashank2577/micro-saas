package com.microsaas.investtracker.service;

import com.microsaas.investtracker.dto.AccountDto;
import com.microsaas.investtracker.entity.BrokerageAccount;
import com.microsaas.investtracker.repository.BrokerageAccountRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrokerageAccountService {
    private final BrokerageAccountRepository brokerageAccountRepository;

    public AccountDto addAccount(UUID portfolioId) {
        UUID tenantId = TenantContext.require();
        BrokerageAccount account = new BrokerageAccount();
        account.setTenantId(tenantId);
        com.microsaas.investtracker.entity.Portfolio portfolio = new com.microsaas.investtracker.entity.Portfolio();
        portfolio.setId(portfolioId);
        account.setPortfolio(portfolio);
        account.setBrokerageName("Manual");
        account.setSyncStatus("PENDING");

        BrokerageAccount saved = brokerageAccountRepository.save(account);

        AccountDto dto = new AccountDto();
        dto.setId(saved.getId());
        dto.setPortfolioId(saved.getPortfolio() != null ? saved.getPortfolio().getId() : null);
        dto.setBrokerageName(saved.getBrokerageName());
        dto.setAccountNumber(saved.getAccountNumber());
        dto.setSyncStatus(saved.getSyncStatus());
        return dto;
    }

    public void syncAccount(UUID accountId) {
        // Trigger async sync via PGMQ
        UUID tenantId = TenantContext.require();
        brokerageAccountRepository.findByIdAndTenantId(accountId, tenantId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        // Assume logic for sync
    }
}