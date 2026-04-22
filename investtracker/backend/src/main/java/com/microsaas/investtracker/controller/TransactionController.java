package com.microsaas.investtracker.controller;

import com.microsaas.investtracker.dto.TransactionDto;
import com.microsaas.investtracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/portfolios/{id}/transactions")
    public ResponseEntity<Page<TransactionDto>> getTransactions(@PathVariable UUID id, Pageable pageable) {
        return ResponseEntity.ok(transactionService.getTransactions(id, pageable));
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDto> createTransaction() {
        return ResponseEntity.ok(transactionService.createTransaction());
    }
}