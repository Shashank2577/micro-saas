package com.microsaas.revopsai.controller;

import com.microsaas.revopsai.model.SalesVelocity;
import com.microsaas.revopsai.service.SalesVelocityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales-velocity")
@RequiredArgsConstructor
public class SalesVelocityController {
    private final SalesVelocityService service;

    @GetMapping
    public ResponseEntity<List<SalesVelocity>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<SalesVelocity> create(@RequestBody SalesVelocity entity) {
        return ResponseEntity.ok(service.create(entity));
    }
}
