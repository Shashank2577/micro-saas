package com.microsaas.onboardflow.service;
import com.microsaas.onboardflow.model.TaskAssignment;
import com.microsaas.onboardflow.repository.TaskAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class TaskAssignmentService {
    private final TaskAssignmentRepository repository;
    public TaskAssignmentService(TaskAssignmentRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<TaskAssignment> findAll(UUID tenantId) { return repository.findByTenantId(tenantId); }
    @Transactional(readOnly = true)
    public TaskAssignment findById(UUID id, UUID tenantId) { return repository.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new RuntimeException("Not found")); }
    public TaskAssignment create(TaskAssignment entity, UUID tenantId) { entity.setId(UUID.randomUUID()); entity.setTenantId(tenantId); return repository.save(entity); }
    public TaskAssignment update(UUID id, TaskAssignment updateContent, UUID tenantId) {
        TaskAssignment existing = findById(id, tenantId);
        if (updateContent.getName() != null) existing.setName(updateContent.getName());
        return repository.save(existing);
    }
}
