package com.microsaas.agencyos.service;

import com.microsaas.agencyos.domain.Project;
import com.microsaas.agencyos.domain.Task;
import com.microsaas.agencyos.exception.ResourceNotFoundException;
import com.microsaas.agencyos.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public List<Task> getAllTasks(String tenantId, UUID projectId) {
        if (projectId != null) {
            return taskRepository.findByTenantIdAndProjectId(tenantId, projectId);
        }
        return taskRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(UUID id, String tenantId) {
        return taskRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Transactional
    public Task createTask(Task task, UUID projectId, String tenantId) {
        Project project = projectService.getProjectById(projectId, tenantId);
        task.setProject(project);
        task.setTenantId(tenantId);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(UUID id, Task taskDetails, String tenantId) {
        Task task = getTaskById(id, tenantId);
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(UUID id, String tenantId) {
        Task task = getTaskById(id, tenantId);
        taskRepository.delete(task);
    }
}
