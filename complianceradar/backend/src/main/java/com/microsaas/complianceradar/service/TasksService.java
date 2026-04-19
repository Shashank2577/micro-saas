package com.microsaas.complianceradar.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.complianceradar.domain.TaskAssignment;
import com.microsaas.complianceradar.repository.TaskAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TasksService {

    private final TaskAssignmentRepository repository;

    @Transactional(readOnly = true)
    public List<TaskAssignment> list() {
        return repository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public TaskAssignment getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("TaskAssignment not found"));
    }

    @Transactional
    public TaskAssignment create(TaskAssignment task) {
        task.setId(UUID.randomUUID());
        task.setTenantId(TenantContext.require());
        task.setCreatedAt(Instant.now());
        return repository.save(task);
    }

    @Transactional
    public TaskAssignment update(UUID id, TaskAssignment updateDetails) {
        TaskAssignment existing = getById(id);
        existing.setName(updateDetails.getName());
        existing.setStatus(updateDetails.getStatus());
        existing.setMetadataJson(updateDetails.getMetadataJson());
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteByIdAndTenantId(id, TenantContext.require());
    }

    @Transactional
    public void validate(UUID id) {
        TaskAssignment existing = getById(id);
        existing.setStatus("VALIDATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }

    @Transactional
    public void simulate(UUID id) {
        TaskAssignment existing = getById(id);
        existing.setStatus("SIMULATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }
}
