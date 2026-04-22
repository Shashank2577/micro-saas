package com.microsaas.investtracker.controller;

import com.microsaas.investtracker.dto.AccountDto;
import com.microsaas.investtracker.service.BrokerageAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BrokerageAccountController {

    private final BrokerageAccountService brokerageAccountService;

    @PostMapping("/portfolios/{id}/accounts")
    public ResponseEntity<AccountDto> addAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(brokerageAccountService.addAccount(id));
    }

    @PostMapping("/accounts/{id}/sync")
    public ResponseEntity<Void> syncAccount(@PathVariable UUID id) {
        brokerageAccountService.syncAccount(id);
        return ResponseEntity.ok().build();
    }
}