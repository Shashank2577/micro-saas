package com.microsaas.financenarrator.controller;

import com.microsaas.financenarrator.model.ApprovalReview;
import com.microsaas.financenarrator.service.ApprovalReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/narratives/approval-reviews")
@RequiredArgsConstructor
public class ApprovalReviewController {
    private final ApprovalReviewService service;

    @GetMapping
    public ResponseEntity<List<ApprovalReview>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<ApprovalReview> create(@RequestBody ApprovalReview dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovalReview> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApprovalReview> update(@PathVariable UUID id, @RequestBody ApprovalReview dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/simulate")
    public ResponseEntity<Void> simulate(@PathVariable UUID id) {
        service.simulate(id);
        return ResponseEntity.ok().build();
    }
}
