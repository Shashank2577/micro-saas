package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.entity.CompensationHistory;
import com.microsaas.compensationos.repository.CompensationHistoryRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@Tag(name = "Compensation History")
public class CompensationHistoryController {

    private final CompensationHistoryRepository compensationHistoryRepository;

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get compensation history for an employee")
    public ResponseEntity<List<CompensationHistory>> getHistory(@PathVariable UUID employeeId) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(compensationHistoryRepository.findByTenantIdAndEmployeeId(tenantId, employeeId));
    }
}
