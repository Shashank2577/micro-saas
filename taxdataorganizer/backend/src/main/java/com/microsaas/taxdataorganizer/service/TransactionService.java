package com.microsaas.taxdataorganizer.service;

import com.microsaas.taxdataorganizer.model.Transaction;
import com.microsaas.taxdataorganizer.model.TransactionCategory;
import com.microsaas.taxdataorganizer.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private static final Set<TransactionCategory> DEDUCTIBLE_CATEGORIES = EnumSet.of(
            TransactionCategory.COGS,
            TransactionCategory.PAYROLL,
            TransactionCategory.RENT,
            TransactionCategory.UTILITIES,
            TransactionCategory.TRAVEL,
            TransactionCategory.MEALS,
            TransactionCategory.SOFTWARE,
            TransactionCategory.EQUIPMENT
    );

    @Transactional
    public Transaction addTransaction(Transaction transaction) {
        transaction.setDeductible(DEDUCTIBLE_CATEGORIES.contains(transaction.getCategory()));
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction categorizeTransaction(UUID transactionId, TransactionCategory category, UUID tenantId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .filter(t -> t.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        transaction.setCategory(category);
        transaction.setDeductible(DEDUCTIBLE_CATEGORIES.contains(category));
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public BigDecimal getDeductibleTotal(UUID taxYearId, UUID tenantId) {
        List<Transaction> transactions = transactionRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId);
        return transactions.stream()
                .filter(Transaction::isDeductible)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
