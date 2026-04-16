package com.microsaas.taxdataorganizer.service;

import com.microsaas.taxdataorganizer.model.TaxSummary;
import com.microsaas.taxdataorganizer.model.Transaction;
import com.microsaas.taxdataorganizer.model.TransactionCategory;
import com.microsaas.taxdataorganizer.repository.TaxSummaryRepository;
import com.microsaas.taxdataorganizer.repository.TransactionRepository;
import com.microsaas.taxdataorganizer.repository.TaxDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxSummaryService {

    private final TaxSummaryRepository taxSummaryRepository;
    private final TransactionRepository transactionRepository;
    private final TaxDocumentRepository taxDocumentRepository;

    @Transactional
    public TaxSummary generateSummary(UUID taxYearId, UUID tenantId) {
        List<Transaction> transactions = transactionRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId);
        
        BigDecimal totalRevenue = transactions.stream()
                .filter(t -> t.getCategory() == TransactionCategory.REVENUE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal totalDeductions = transactions.stream()
                .filter(Transaction::isDeductible)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal netTaxableIncome = totalRevenue.subtract(totalDeductions);
        
        int transactionCount = transactions.size();
        int documentCount = taxDocumentRepository.findAllByTaxYearIdAndTenantId(taxYearId, tenantId).size();
        
        TaxSummary summary = taxSummaryRepository.findByTaxYearIdAndTenantId(taxYearId, tenantId)
                .orElse(TaxSummary.builder()
                        .taxYearId(taxYearId)
                        .tenantId(tenantId)
                        .build());
                        
        summary.setTotalRevenue(totalRevenue);
        summary.setTotalDeductions(totalDeductions);
        summary.setNetTaxableIncome(netTaxableIncome);
        summary.setTransactionCount(transactionCount);
        summary.setDocumentCount(documentCount);
        summary.setGeneratedAt(LocalDateTime.now());
        
        return taxSummaryRepository.save(summary);
    }
}
