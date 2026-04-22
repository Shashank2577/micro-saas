package com.microsaas.investtracker.service;

import com.microsaas.investtracker.dto.TransactionDto;
import com.microsaas.investtracker.entity.Transaction;
import com.microsaas.investtracker.repository.TransactionRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Page<TransactionDto> getTransactions(UUID portfolioId, Pageable pageable) {
        // Mock implementation
        UUID tenantId = TenantContext.require();
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    public TransactionDto createTransaction() {
        // Mock implementation
        return new TransactionDto();
    }
}