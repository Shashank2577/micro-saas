package com.microsaas.agencyos.controller;

import com.microsaas.agencyos.domain.Task;
import com.microsaas.agencyos.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) UUID projectId, @RequestHeader("X-Tenant-ID") String tenantId) {
        return taskService.getAllTasks(tenantId, projectId);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        return taskService.getTaskById(id, tenantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task, @RequestParam UUID projectId, @RequestHeader("X-Tenant-ID") String tenantId) {
        return taskService.createTask(task, projectId, tenantId);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable UUID id, @RequestBody Task task, @RequestHeader("X-Tenant-ID") String tenantId) {
        return taskService.updateTask(id, task, tenantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        taskService.deleteTask(id, tenantId);
    }
}
