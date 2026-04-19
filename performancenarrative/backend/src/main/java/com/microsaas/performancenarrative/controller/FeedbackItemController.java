package com.microsaas.performancenarrative.controller;

import com.microsaas.performancenarrative.entity.FeedbackItem;
import com.microsaas.performancenarrative.service.FeedbackItemService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/performance/feedback-items")
public class FeedbackItemController {

    private final FeedbackItemService service;

    public FeedbackItemController(FeedbackItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<FeedbackItem> list() {
        return service.list();
    }

    @PostMapping
    public FeedbackItem create(@RequestBody FeedbackItem entity) {
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public FeedbackItem getById(@PathVariable UUID id) {
        return service.getById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PatchMapping("/{id}")
    public FeedbackItem update(@PathVariable UUID id, @RequestBody FeedbackItem details) {
        return service.update(id, details);
    }

    @PostMapping("/{id}/validate")
    public boolean validate(@PathVariable UUID id) {
        return service.validate(id);
    }
}
