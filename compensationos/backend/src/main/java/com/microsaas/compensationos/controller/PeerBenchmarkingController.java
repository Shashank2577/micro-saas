package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.entity.EmployeeCompensation;
import com.microsaas.compensationos.repository.EmployeeCompensationRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/peer-benchmark")
@RequiredArgsConstructor
@Tag(name = "Peer Benchmarking")
public class PeerBenchmarkingController {

    private final EmployeeCompensationRepository employeeCompensationRepository;

    @GetMapping
    @Operation(summary = "Get peer benchmarking data by role")
    public ResponseEntity<List<EmployeeCompensation>> getPeerBenchmark(@RequestParam String role) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(employeeCompensationRepository.findByTenantIdAndRole(tenantId, role));
    }
}
