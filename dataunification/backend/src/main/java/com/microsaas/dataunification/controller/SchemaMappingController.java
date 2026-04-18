package com.microsaas.dataunification.controller;

import com.microsaas.dataunification.model.SchemaMapping;
import com.microsaas.dataunification.service.SchemaMappingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/mappings")
public class SchemaMappingController {
    private final SchemaMappingService service;

    public SchemaMappingController(SchemaMappingService service) {
        this.service = service;
    }

    @GetMapping
    public List<SchemaMapping> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SchemaMapping getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public SchemaMapping create(@RequestBody SchemaMapping mapping) {
        return service.create(mapping);
    }
    
    @PostMapping("/suggest")
    public String suggest(@RequestBody Map<String, String> schemas) {
        return service.suggestMapping(schemas.get("source"), schemas.get("target"));
    }
}
