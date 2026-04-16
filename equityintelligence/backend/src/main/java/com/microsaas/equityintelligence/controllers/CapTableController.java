package com.microsaas.equityintelligence.controllers;

import com.microsaas.equityintelligence.services.CapTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cap-table")
@RequiredArgsConstructor
public class CapTableController {

    private final CapTableService capTableService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCapTable(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(capTableService.getCapTable(tenantId));
    }
}
