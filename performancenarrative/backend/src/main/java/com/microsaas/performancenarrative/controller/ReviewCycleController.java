package com.microsaas.performancenarrative.controller;

import com.microsaas.performancenarrative.entity.ReviewCycle;
import com.microsaas.performancenarrative.service.ReviewCycleService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/performance/review-cycles")
public class ReviewCycleController {

    private final ReviewCycleService service;

    public ReviewCycleController(ReviewCycleService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReviewCycle> list() {
        return service.list();
    }

    @PostMapping
    public ReviewCycle create(@RequestBody ReviewCycle entity) {
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public ReviewCycle getById(@PathVariable UUID id) {
        return service.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PatchMapping("/{id}")
    public ReviewCycle update(@PathVariable UUID id, @RequestBody ReviewCycle details) {
        return service.update(id, details);
    }

    @PostMapping("/{id}/validate")
    public boolean validate(@PathVariable UUID id) {
        return service.validate(id);
    }
}
