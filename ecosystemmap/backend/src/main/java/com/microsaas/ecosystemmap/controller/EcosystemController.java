package com.microsaas.ecosystemmap.controller;

import com.microsaas.ecosystemmap.entity.Ecosystem;
import com.microsaas.ecosystemmap.service.EcosystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ecosystems")
@RequiredArgsConstructor
public class EcosystemController {

    private final EcosystemService ecosystemService;

    @GetMapping
    public ResponseEntity<List<Ecosystem>> getAllEcosystems() {
        return ResponseEntity.ok(ecosystemService.getAllEcosystems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ecosystem> getEcosystemById(@PathVariable UUID id) {
        return ResponseEntity.ok(ecosystemService.getEcosystemById(id));
    }

    @PostMapping
    public ResponseEntity<Ecosystem> createEcosystem(@RequestBody Ecosystem ecosystem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ecosystemService.createEcosystem(ecosystem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ecosystem> updateEcosystem(@PathVariable UUID id, @RequestBody Ecosystem ecosystem) {
        return ResponseEntity.ok(ecosystemService.updateEcosystem(id, ecosystem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEcosystem(@PathVariable UUID id) {
        ecosystemService.deleteEcosystem(id);
        return ResponseEntity.noContent().build();
    }
}
