package com.microsaas.dataunification.controller;

import com.microsaas.dataunification.model.DataSource;
import com.microsaas.dataunification.service.DataSourceService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sources")
public class DataSourceController {
    private final DataSourceService service;

    public DataSourceController(DataSourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<DataSource> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public DataSource getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public DataSource create(@RequestBody DataSource ds) {
        return service.create(ds);
    }
}
