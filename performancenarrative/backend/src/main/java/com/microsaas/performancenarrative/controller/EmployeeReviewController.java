package com.microsaas.performancenarrative.controller;

import com.microsaas.performancenarrative.entity.EmployeeReview;
import com.microsaas.performancenarrative.service.EmployeeReviewService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/performance/employee-reviews")
public class EmployeeReviewController {

    private final EmployeeReviewService service;

    public EmployeeReviewController(EmployeeReviewService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmployeeReview> list() {
        return service.list();
    }

    @PostMapping
    public EmployeeReview create(@RequestBody EmployeeReview entity) {
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public EmployeeReview getById(@PathVariable UUID id) {
        return service.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PatchMapping("/{id}")
    public EmployeeReview update(@PathVariable UUID id, @RequestBody EmployeeReview details) {
        return service.update(id, details);
    }

    @PostMapping("/{id}/validate")
    public boolean validate(@PathVariable UUID id) {
        return service.validate(id);
    }
}
