package com.microsaas.retailintelligence.controller;

import com.microsaas.retailintelligence.dto.CreateSkuRequest;
import com.microsaas.retailintelligence.dto.SkuDto;
import com.microsaas.retailintelligence.service.RetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skus")
@RequiredArgsConstructor
public class RetailController {

    private final RetailService retailService;

    @GetMapping
    public ResponseEntity<List<SkuDto>> getSkus() {
        return ResponseEntity.ok(retailService.getSkus());
    }

    @PostMapping
    public ResponseEntity<SkuDto> createSku(@RequestBody CreateSkuRequest request) {
        return ResponseEntity.ok(retailService.createSku(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkuDto> getSku(@PathVariable UUID id) {
        return ResponseEntity.ok(retailService.getSku(id));
    }
}
