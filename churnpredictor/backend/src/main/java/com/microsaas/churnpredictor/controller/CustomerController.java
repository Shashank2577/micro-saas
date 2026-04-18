package com.microsaas.churnpredictor.controller;

import com.microsaas.churnpredictor.dto.CustomerDto;
import com.microsaas.churnpredictor.dto.CustomerHealthScoreDto;
import com.microsaas.churnpredictor.dto.ChurnPredictionDto;
import com.microsaas.churnpredictor.service.CustomerService;
import com.microsaas.churnpredictor.service.HealthScoreService;
import com.microsaas.churnpredictor.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final HealthScoreService healthScoreService;
    private final PredictionService predictionService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto dto) {
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }

    @GetMapping("/{id}/health")
    public ResponseEntity<List<CustomerHealthScoreDto>> getHealthScoreHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(healthScoreService.getHealthScoreHistory(id));
    }

    @PostMapping("/{id}/health/recalculate")
    public ResponseEntity<CustomerHealthScoreDto> recalculateHealthScore(@PathVariable UUID id) {
        return ResponseEntity.ok(healthScoreService.recalculateHealthScore(id));
    }

    @GetMapping("/{id}/predictions")
    public ResponseEntity<List<ChurnPredictionDto>> getPredictionHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(predictionService.getPredictionHistory(id));
    }
}
