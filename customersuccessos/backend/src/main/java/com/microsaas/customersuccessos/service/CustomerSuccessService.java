package com.microsaas.customersuccessos.service;

import com.microsaas.customersuccessos.model.*;
import com.microsaas.customersuccessos.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerSuccessService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HealthScoreRepository healthScoreRepository;

    @Autowired
    private ExpansionOpportunityRepository expansionOpportunityRepository;

    public List<Account> getAccounts() {
        return accountRepository.findByTenantId(TenantContext.require());
    }

    public Account createAccount(Account account) {
        account.setId(UUID.randomUUID());
        account.setTenantId(TenantContext.require());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public Account getAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        if (!account.getTenantId().equals(TenantContext.require())) {
            throw new RuntimeException("Unauthorized");
        }
        return account;
    }

    public HealthScore getLatestHealthScore(UUID accountId) {
        List<HealthScore> scores = healthScoreRepository.findByTenantIdAndAccountIdOrderByRecordedAtDesc(TenantContext.require(), accountId);
        return scores.isEmpty() ? null : scores.get(0);
    }

    public HealthScore recordHealthScore(UUID accountId, HealthScore score) {
        score.setId(UUID.randomUUID());
        score.setTenantId(TenantContext.require());
        score.setAccountId(accountId);
        score.setRecordedAt(LocalDateTime.now());
        return healthScoreRepository.save(score);
    }

    public List<ExpansionOpportunity> getExpansionOpportunities(UUID accountId) {
        return expansionOpportunityRepository.findByTenantIdAndAccountId(TenantContext.require(), accountId);
    }

    public List<ExpansionOpportunity> getAllExpansionOpportunities() {
        return expansionOpportunityRepository.findByTenantId(TenantContext.require());
    }

    public ExpansionOpportunity createExpansionOpportunity(UUID accountId, ExpansionOpportunity opportunity) {
        opportunity.setId(UUID.randomUUID());
        opportunity.setTenantId(TenantContext.require());
        opportunity.setAccountId(accountId);
        opportunity.setCreatedAt(LocalDateTime.now());
        if (opportunity.getStatus() == null) {
            opportunity.setStatus("IDENTIFIED");
        }
        return expansionOpportunityRepository.save(opportunity);
    }
}
