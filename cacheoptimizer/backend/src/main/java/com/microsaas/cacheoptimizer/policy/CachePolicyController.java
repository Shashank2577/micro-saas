package com.microsaas.cacheoptimizer.policy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/policies")
public class CachePolicyController {

    private final CachePolicyService service;

    public CachePolicyController(CachePolicyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CachePolicyDto> createPolicy(@RequestBody CachePolicyDto dto) {
        return ResponseEntity.ok(service.createPolicy(dto));
    }

    @GetMapping
    public ResponseEntity<List<CachePolicyDto>> getPolicies() {
        return ResponseEntity.ok(service.getPolicies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CachePolicyDto> updatePolicy(@PathVariable UUID id, @RequestBody CachePolicyDto dto) {
        return ResponseEntity.ok(service.updatePolicy(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable UUID id) {
        service.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}
