package com.microsaas.realestateitel.controller;

import com.microsaas.realestateitel.domain.Comparable;
import com.microsaas.realestateitel.service.ComparableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties/{propertyId}/comparables")
@RequiredArgsConstructor
public class ComparableController {

    private final ComparableService comparableService;

    @PostMapping("/generate")
    public ResponseEntity<List<Comparable>> generateComparables(@PathVariable UUID propertyId) {
        return ResponseEntity.ok(comparableService.generateComparables(propertyId));
    }

    @GetMapping
    public ResponseEntity<List<Comparable>> getComparables(@PathVariable UUID propertyId) {
        return ResponseEntity.ok(comparableService.getComparablesForProperty(propertyId));
    }
}
