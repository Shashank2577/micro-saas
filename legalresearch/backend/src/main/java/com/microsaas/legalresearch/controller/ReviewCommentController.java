package com.microsaas.legalresearch.controller;

import com.microsaas.legalresearch.domain.ReviewComment;
import com.microsaas.legalresearch.service.ReviewCommentService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/research/review-comments")
@RequiredArgsConstructor
public class ReviewCommentController {
    private final ReviewCommentService service;

    @GetMapping
    public ResponseEntity<List<ReviewComment>> findAll() {
        return ResponseEntity.ok(service.findAll(TenantContext.require()));
    }

    @PostMapping
    public ResponseEntity<ReviewComment> create(@RequestBody ReviewComment entity) {
        entity.setTenantId(TenantContext.require());
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewComment> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, TenantContext.require()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewComment> update(@PathVariable UUID id, @RequestBody ReviewComment entity) {
        return ResponseEntity.ok(service.update(id, entity, TenantContext.require()));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id, TenantContext.require()));
    }
}
