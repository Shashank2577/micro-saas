package com.microsaas.taskqueue.controller;

import com.microsaas.taskqueue.domain.Job;
import com.microsaas.taskqueue.domain.JobHistory;
import com.microsaas.taskqueue.domain.JobStatus;
import com.microsaas.taskqueue.dto.JobRequest;
import com.microsaas.taskqueue.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Job> enqueueJob(@RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.enqueueJob(request));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Job>> enqueueBatch(@RequestBody List<JobRequest> requests) {
        return ResponseEntity.ok(jobService.enqueueBatch(requests));
    }

    @GetMapping
    public ResponseEntity<List<Job>> getJobs(@RequestParam(required = false) JobStatus status) {
        return ResponseEntity.ok(jobService.getJobs(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.getJob(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<JobHistory>> getJobHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.getJobHistory(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Job> cancelJob(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.cancelJob(id));
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<Job> retryJob(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.retryJob(id));
    }
}
