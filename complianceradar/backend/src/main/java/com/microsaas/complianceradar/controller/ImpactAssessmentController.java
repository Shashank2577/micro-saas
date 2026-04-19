package com.microsaas.complianceradar.controller;

import com.microsaas.complianceradar.domain.ImpactAssessment;
import com.microsaas.complianceradar.service.ImpactAssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regulations/impact-assessments")
@RequiredArgsConstructor
public class ImpactAssessmentController {

    private final ImpactAssessmentService service;

    @GetMapping
    public ResponseEntity<List<ImpactAssessment>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImpactAssessment> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ImpactAssessment> create(@RequestBody ImpactAssessment assessment) {
        return ResponseEntity.ok(service.create(assessment));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ImpactAssessment> update(@PathVariable UUID id, @RequestBody ImpactAssessment updateDetails) {
        return ResponseEntity.ok(service.update(id, updateDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
