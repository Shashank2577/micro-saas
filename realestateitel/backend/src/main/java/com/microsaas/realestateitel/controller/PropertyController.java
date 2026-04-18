package com.microsaas.realestateitel.controller;

import com.microsaas.realestateitel.domain.Property;
import com.microsaas.realestateitel.dto.PropertyDto;
import com.microsaas.realestateitel.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody PropertyDto dto) {
        return ResponseEntity.ok(propertyService.createProperty(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getProperty(@PathVariable UUID id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @GetMapping
    public ResponseEntity<List<Property>> listProperties(@RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(propertyService.listPropertiesByStatus(status));
        }
        return ResponseEntity.ok(propertyService.listProperties());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable UUID id, @RequestBody PropertyDto dto) {
        return ResponseEntity.ok(propertyService.updateProperty(id, dto));
    }
}
