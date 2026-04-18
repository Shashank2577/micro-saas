package com.microsaas.cacheoptimizer.cache;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cache")
public class CacheOperationsController {

    private final CacheService service;

    public CacheOperationsController(CacheService service) {
        this.service = service;
    }

    @GetMapping("/{namespace}/{key}")
    public ResponseEntity<String> getCache(@PathVariable String namespace, @PathVariable String key) {
        String value = service.get(namespace, key);
        if (value != null) {
            return ResponseEntity.ok(value);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{namespace}/{key}")
    public ResponseEntity<Void> putCache(@PathVariable String namespace, @PathVariable String key, @RequestBody String value) {
        service.put(namespace, key, value);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{namespace}/{key}")
    public ResponseEntity<Void> invalidateCache(@PathVariable String namespace, @PathVariable String key) {
        service.invalidate(namespace, key);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/warm")
    public ResponseEntity<Void> warmCache(@RequestBody WarmRequest request) {
        service.warm(request.getNamespace(), request.getKeys());
        return ResponseEntity.ok().build();
    }
}
