package com.microsaas.cashflowanalyzer.service;

import com.microsaas.cashflowanalyzer.model.CashFlowSnapshot;
import com.microsaas.cashflowanalyzer.model.Transaction;
import com.microsaas.cashflowanalyzer.repository.CashFlowSnapshotRepository;
import com.microsaas.cashflowanalyzer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CashFlowAnalysisService {

    @Autowired
    private CashFlowSnapshotRepository cashFlowSnapshotRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public CashFlowSnapshot generateCashFlowStatement(String tenantId, LocalDate month) {
        // Mock generation
        List<Transaction> transactions = transactionRepository.findByTenantId(tenantId);

        BigDecimal totalIncome = BigDecimal.valueOf(8500.00);
        BigDecimal totalExpenses = BigDecimal.valueOf(6200.00);
        BigDecimal savingsRate = BigDecimal.valueOf(0.27); // 27%

        CashFlowSnapshot snapshot = new CashFlowSnapshot();
        snapshot.setTenantId(tenantId);
        snapshot.setMonth(month);
        snapshot.setTotalIncome(totalIncome);
        snapshot.setTotalExpenses(totalExpenses);
        snapshot.setSavingsRate(savingsRate);
        snapshot.setCreatedAt(LocalDateTime.now());

        return cashFlowSnapshotRepository.save(snapshot);
    }
}
