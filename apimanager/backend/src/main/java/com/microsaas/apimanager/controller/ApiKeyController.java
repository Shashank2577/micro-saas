package com.microsaas.apimanager.controller;

import com.microsaas.apimanager.model.ApiKey;
import com.microsaas.apimanager.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @GetMapping
    public ResponseEntity<List<ApiKey>> listKeys(@RequestParam UUID projectId) {
        return ResponseEntity.ok(apiKeyService.getKeys(projectId));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> generateKey(@RequestBody ApiKey key) {
        String rawKey = apiKeyService.generateKey(key);
        return ResponseEntity.ok(Map.of("key", rawKey));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revokeKey(@PathVariable UUID id) {
        if (apiKeyService.revokeKey(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
