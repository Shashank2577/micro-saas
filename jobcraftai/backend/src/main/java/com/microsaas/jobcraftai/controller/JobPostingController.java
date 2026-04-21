package com.microsaas.jobcraftai.controller;

import com.microsaas.jobcraftai.model.JobPosting;
import com.microsaas.jobcraftai.service.JobCraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobCraftService service;

    @GetMapping
    public ResponseEntity<List<JobPosting>> getAll() {
        return ResponseEntity.ok(service.getAllForTenant());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JobPosting> create(@RequestBody JobPosting posting) {
        return ResponseEntity.ok(service.create(posting));
    }

    @PostMapping("/{id}/optimize")
    public ResponseEntity<JobPosting> optimize(@PathVariable UUID id) {
        return ResponseEntity.ok(service.optimize(id));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<JobPosting> publish(@PathVariable UUID id) {
        return ResponseEntity.ok(service.publish(id));
    }
}
