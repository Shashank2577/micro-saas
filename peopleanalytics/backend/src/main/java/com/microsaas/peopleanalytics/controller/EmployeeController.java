package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.service.EmployeeService;
import com.microsaas.peopleanalytics.service.HRISIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/people-analytics/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final HRISIntegrationService hrisIntegrationService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/sync")
    public ResponseEntity<Void> bulkSync(@RequestBody List<Map<String, String>> data) {
        hrisIntegrationService.bulkSync(data);
        return ResponseEntity.ok().build();
    }
}
