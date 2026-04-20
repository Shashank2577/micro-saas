package com.microsaas.experimentengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.experimentengine.domain.model.Experiment;
import com.microsaas.experimentengine.domain.repository.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/experiments")
public class ExperimentController {

    private final ExperimentRepository experimentRepository;

    @Autowired
    public ExperimentController(ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Experiment>> getAllExperiments() {
        return ResponseEntity.ok(experimentRepository.findByTenantId(TenantContext.require()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Experiment> getExperimentById(@PathVariable UUID id) {
        return experimentRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Experiment> createExperiment(@RequestBody Experiment experiment) {
        experiment.setTenantId(TenantContext.require());
        return ResponseEntity.ok(experimentRepository.save(experiment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Experiment> updateExperiment(@PathVariable UUID id, @RequestBody Experiment experiment) {
        return experimentRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(existing -> {
                    experiment.setId(existing.getId());
                    experiment.setTenantId(existing.getTenantId());
                    return ResponseEntity.ok(experimentRepository.save(experiment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperiment(@PathVariable UUID id) {
        return experimentRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(existing -> {
                    experimentRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
