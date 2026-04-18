package com.microsaas.budgetmaster.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetmaster.domain.Transaction;
import com.microsaas.budgetmaster.dto.Requests.CreateTransactionRequest;
import com.microsaas.budgetmaster.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AlertService alertService;

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByBudget(UUID budgetId) {
        return transactionRepository.findAllByBudgetIdAndTenantId(budgetId, TenantContext.require());
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByCategory(UUID categoryId) {
        return transactionRepository.findAllByCategoryIdAndTenantId(categoryId, TenantContext.require());
    }

    @Transactional
    public Transaction recordTransaction(CreateTransactionRequest request) {
        Transaction tx = Transaction.builder()
                .tenantId(TenantContext.require())
                .budgetId(request.getBudgetId())
                .categoryId(request.getCategoryId())
                .amount(request.getAmount())
                .date(request.getDate())
                .description(request.getDescription())
                .status("CLEARED")
                .build();
        Transaction saved = transactionRepository.save(tx);
        
        if (request.getCategoryId() != null) {
            alertService.checkAlerts(request.getCategoryId());
        }
        
        return saved;
    }
}
