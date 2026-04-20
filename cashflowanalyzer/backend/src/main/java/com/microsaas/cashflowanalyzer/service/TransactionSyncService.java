package com.microsaas.cashflowanalyzer.service;

import com.microsaas.cashflowanalyzer.model.Account;
import com.microsaas.cashflowanalyzer.model.Transaction;
import com.microsaas.cashflowanalyzer.repository.AccountRepository;
import com.microsaas.cashflowanalyzer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionSyncService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionSyncService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionCategorizationService categorizationService;

    public void syncTransactions(String tenantId) {
        List<Account> accounts = accountRepository.findByTenantId(tenantId);
        logger.info("Syncing transactions for tenant: {} for {} accounts", tenantId, accounts.size());

        for (Account account : accounts) {
            // Mock transaction sync from Plaid
            Transaction tx = new Transaction();
            tx.setTenantId(tenantId);
            tx.setAccount(account);
            tx.setPlaidTransactionId("mock_tx_" + UUID.randomUUID());
            tx.setAmount(new BigDecimal("12.50"));
            tx.setDate(LocalDate.now());
            tx.setName("Coffee Shop");
            tx.setMerchantName("Coffee Shop Inc.");
            tx.setPending(false);
            tx.setIsRecurring(false);
            tx.setCreatedAt(LocalDateTime.now());

            Transaction savedTx = transactionRepository.save(tx);
            categorizationService.categorizeTransaction(savedTx);
        }
    }
}
