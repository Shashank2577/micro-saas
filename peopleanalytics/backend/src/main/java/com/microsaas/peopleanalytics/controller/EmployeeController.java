package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.service.EmployeeService;
import com.microsaas.peopleanalytics.dto.EmployeeDto;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(employeeService.getEmployees(tenantId));
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(employeeService.createEmployee(tenantId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }
}
