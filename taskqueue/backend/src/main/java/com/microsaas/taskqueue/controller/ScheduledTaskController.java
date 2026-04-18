package com.microsaas.taskqueue.controller;

import com.microsaas.taskqueue.domain.ScheduledTask;
import com.microsaas.taskqueue.dto.ScheduledTaskRequest;
import com.microsaas.taskqueue.service.SchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scheduled-tasks")
public class ScheduledTaskController {
    private final SchedulerService schedulerService;

    public ScheduledTaskController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping
    public ResponseEntity<ScheduledTask> createScheduledTask(@RequestBody ScheduledTaskRequest request) {
        return ResponseEntity.ok(schedulerService.createScheduledTask(request));
    }

    @GetMapping
    public ResponseEntity<List<ScheduledTask>> getScheduledTasks() {
        return ResponseEntity.ok(schedulerService.getScheduledTasks());
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<ScheduledTask> toggleTask(@PathVariable UUID id) {
        return ResponseEntity.ok(schedulerService.toggleTask(id));
    }
}
