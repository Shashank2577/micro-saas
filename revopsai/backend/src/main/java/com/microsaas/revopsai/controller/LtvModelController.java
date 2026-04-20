package com.microsaas.revopsai.controller;

import com.microsaas.revopsai.model.LtvModel;
import com.microsaas.revopsai.service.LtvModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ltv-models")
@RequiredArgsConstructor
public class LtvModelController {
    private final LtvModelService service;

    @GetMapping
    public ResponseEntity<List<LtvModel>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<LtvModel> create(@RequestBody LtvModel entity) {
        return ResponseEntity.ok(service.create(entity));
    }
}
