package com.microsaas.documentvault.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentvault.model.StorageQuota;
import com.microsaas.documentvault.service.QuotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/quota")
public class QuotaController {

    private final QuotaService quotaService;

    public QuotaController(QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    private UUID getTenantId() {
        return TenantContext.require();
    }

    @GetMapping
    public ResponseEntity<StorageQuota> getQuota() {
        return ResponseEntity.ok(quotaService.getQuota(getTenantId()));
    }
}
