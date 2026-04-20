package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.Account;
import com.microsaas.cashflowanalyzer.service.AccountAggregationService;
import com.microsaas.cashflowanalyzer.service.TransactionSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountAggregationService accountAggregationService;

    @Autowired
    private TransactionSyncService transactionSyncService;

    @PostMapping("/connect")
    public ResponseEntity<Account> connectAccount(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Map<String, String> payload) {
        String publicToken = payload.get("publicToken");
        return ResponseEntity.ok(accountAggregationService.connectAccount(tenantId, publicToken));
    }

    @PostMapping("/disconnect/{accountId}")
    public ResponseEntity<Void> disconnectAccount(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID accountId) {
        accountAggregationService.disconnectAccount(accountId, tenantId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sync")
    public ResponseEntity<Void> syncTransactions(@RequestHeader("X-Tenant-ID") String tenantId) {
        transactionSyncService.syncTransactions(tenantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Account>> listAccounts(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(accountAggregationService.listAccounts(tenantId));
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Account> getAccountBalance(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID accountId) {
        return ResponseEntity.ok(accountAggregationService.getAccountBalance(accountId, tenantId));
    }
}
