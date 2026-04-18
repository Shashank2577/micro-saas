package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.dto.BenchmarkingResponse;
import com.microsaas.compensationos.dto.EmployeeCompDto;
import com.microsaas.compensationos.service.BenchmarkingService;
import com.microsaas.compensationos.service.EmployeeCompensationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Compensation")
public class EmployeeCompensationController {

    private final EmployeeCompensationService employeeCompensationService;
    private final BenchmarkingService benchmarkingService;

    @GetMapping
    @Operation(summary = "Get employee compensation records")
    public ResponseEntity<List<EmployeeCompDto>> getEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String role) {
        return ResponseEntity.ok(employeeCompensationService.getEmployees(department, role));
    }

    @PostMapping
    @Operation(summary = "Save or update employee compensation")
    public ResponseEntity<EmployeeCompDto> saveEmployee(@RequestBody EmployeeCompDto dto) {
        return ResponseEntity.ok(employeeCompensationService.saveEmployee(dto));
    }

    @GetMapping("/{id}/benchmark")
    @Operation(summary = "Benchmark employee compensation against market data")
    public ResponseEntity<BenchmarkingResponse> getBenchmark(@PathVariable UUID id) {
        return ResponseEntity.ok(benchmarkingService.getEmployeeBenchmark(id));
    }
}
