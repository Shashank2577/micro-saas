package com.microsaas.constructioniq.project;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(UUID projectId, Task task) {
        task.setProjectId(projectId);
        task.setTenantId(TenantContext.require());
        task.setCreatedAt(OffsetDateTime.now());
        task.setUpdatedAt(OffsetDateTime.now());
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasks(UUID projectId) {
        return taskRepository.findByProjectIdAndTenantId(projectId, TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Task getTask(UUID id) {
        return taskRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task updateTask(UUID id, Task taskUpdate) {
        Task task = getTask(id);
        task.setTitle(taskUpdate.getTitle());
        task.setDescription(taskUpdate.getDescription());
        task.setAssignedTo(taskUpdate.getAssignedTo());
        task.setStatus(taskUpdate.getStatus());
        task.setPriority(taskUpdate.getPriority());
        task.setDueDate(taskUpdate.getDueDate());
        task.setUpdatedAt(OffsetDateTime.now());
        return taskRepository.save(task);
    }

    public void deleteTask(UUID id) {
        Task task = getTask(id);
        taskRepository.delete(task);
    }
}
