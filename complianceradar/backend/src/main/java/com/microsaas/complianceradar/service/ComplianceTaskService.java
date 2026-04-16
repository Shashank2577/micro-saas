package com.microsaas.complianceradar.service;

import com.microsaas.complianceradar.domain.ComplianceTask;
import com.microsaas.complianceradar.repository.ComplianceTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplianceTaskService {
    private final ComplianceTaskRepository repository;

    @Transactional
    public ComplianceTask createTask(UUID gapId, String description, LocalDate dueDate, String assignedTo, UUID tenantId) {
        ComplianceTask task = ComplianceTask.builder()
                .gapId(gapId)
                .description(description)
                .dueDate(dueDate)
                .assignedTo(assignedTo)
                .tenantId(tenantId)
                .build();
        return repository.save(task);
    }

    public List<ComplianceTask> listTasks(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional
    public ComplianceTask completeTask(UUID id, UUID tenantId) {
        ComplianceTask task = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Compliance Task not found"));
        task.setCompletedAt(ZonedDateTime.now());
        return repository.save(task);
    }
}
