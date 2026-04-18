package com.microsaas.restaurantintel.controller;

import com.microsaas.restaurantintel.domain.MenuItem;
import com.microsaas.restaurantintel.service.MenuIntelligenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-items")
public class MenuIntelligenceController {

    private final MenuIntelligenceService service;

    public MenuIntelligenceController(MenuIntelligenceService service) {
        this.service = service;
    }

    @GetMapping
    public List<MenuItem> getAllMenuItems() {
        return service.getAllMenuItems();
    }

    @PostMapping
    public MenuItem createMenuItem(@RequestBody MenuItem item) {
        return service.createMenuItem(item);
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeMenu() {
        return ResponseEntity.ok(service.analyzeMenu());
    }
}
