package com.microsaas.constructioniq.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@PathVariable UUID projectId, @RequestBody Task task) {
        return taskService.createTask(projectId, task);
    }

    @GetMapping
    public List<Task> getTasks(@PathVariable UUID projectId) {
        return taskService.getTasks(projectId);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable UUID id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
    }
}
