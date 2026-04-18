package com.microsaas.authvault.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
public class AuthVaultAuditController {

    @GetMapping
    public ResponseEntity<List<String>> listLogs() {
        return ResponseEntity.ok(List.of("Log entry 1", "Log entry 2"));
    }
}
