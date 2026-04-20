package com.microsaas.cashflowanalyzer.service;

import com.microsaas.cashflowanalyzer.model.Account;
import com.microsaas.cashflowanalyzer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class AccountAggregationService {
    private static final Logger logger = LoggerFactory.getLogger(AccountAggregationService.class);

    @Autowired
    private AccountRepository accountRepository;

    public Account connectAccount(String tenantId, String plaidPublicToken) {
        // Mock Plaid connection logic here
        logger.info("Connecting account for tenant: {} using token: {}", tenantId, plaidPublicToken);

        Account account = new Account();
        account.setTenantId(tenantId);
        account.setPlaidAccountId("mock_plaid_account_id_" + UUID.randomUUID());
        account.setPlaidItemId("mock_plaid_item_id");
        account.setName("Mock Checking Account");
        account.setMask("1234");
        account.setType("depository");
        account.setSubtype("checking");
        account.setBalanceCurrent(new java.math.BigDecimal("1500.00"));
        account.setBalanceAvailable(new java.math.BigDecimal("1400.00"));
        account.setIsoCurrencyCode("USD");
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    public void disconnectAccount(UUID accountId, String tenantId) {
        accountRepository.findById(accountId).ifPresent(account -> {
            if (account.getTenantId().equals(tenantId)) {
                logger.info("Disconnecting account: {}", accountId);
                accountRepository.delete(account);
            }
        });
    }

    public List<Account> listAccounts(String tenantId) {
        return accountRepository.findByTenantId(tenantId);
    }

    public Account getAccountBalance(UUID accountId, String tenantId) {
        return accountRepository.findById(accountId)
                .filter(a -> a.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));
    }
}
