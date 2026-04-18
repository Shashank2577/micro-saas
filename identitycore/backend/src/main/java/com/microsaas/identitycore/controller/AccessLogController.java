package com.microsaas.identitycore.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.identitycore.model.AccessLog;
import com.microsaas.identitycore.service.AccessLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/access-logs")
public class AccessLogController {

    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    private UUID getTenantId() {
        return UUID.fromString(TenantContext.require().toString());
    }

    @GetMapping
    public ResponseEntity<List<AccessLog>> listAccessLogs() {
        return ResponseEntity.ok(accessLogService.getLogsByTenant(getTenantId()));
    }

    @PostMapping
    public ResponseEntity<AccessLog> ingestAccessLog(@RequestBody AccessLog log) {
        return ResponseEntity.ok(accessLogService.createLog(getTenantId(), log));
    }
}
