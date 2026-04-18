package com.microsaas.taskqueue.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.taskqueue.domain.ScheduledTask;
import com.microsaas.taskqueue.dto.JobRequest;
import com.microsaas.taskqueue.dto.ScheduledTaskRequest;
import com.microsaas.taskqueue.repository.ScheduledTaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SchedulerService {
    private final ScheduledTaskRepository scheduledTaskRepository;
    private final JobService jobService;

    public SchedulerService(ScheduledTaskRepository scheduledTaskRepository, JobService jobService) {
        this.scheduledTaskRepository = scheduledTaskRepository;
        this.jobService = jobService;
    }

    @Transactional
    public ScheduledTask createScheduledTask(ScheduledTaskRequest request) {
        UUID tenantId = TenantContext.require();
        ScheduledTask task = new ScheduledTask();
        task.setTenantId(tenantId);
        task.setName(request.getName());
        task.setCronExpression(request.getCronExpression());
        task.setJobName(request.getJobName());
        task.setPayloadTemplate(request.getPayloadTemplate());
        task.setPriority(request.getPriority());
        task.setActive(request.getActive() != null ? request.getActive() : true);

        return scheduledTaskRepository.save(task);
    }

    public List<ScheduledTask> getScheduledTasks() {
        UUID tenantId = TenantContext.require();
        return scheduledTaskRepository.findByTenantId(tenantId);
    }

    @Transactional
    public ScheduledTask toggleTask(UUID id) {
        UUID tenantId = TenantContext.require();
        ScheduledTask task = scheduledTaskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!task.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Task not found");
        }
        task.setActive(!task.getActive());
        return scheduledTaskRepository.save(task);
    }

    // In a real system, this would parse cron expressions and trigger appropriately.
    // We mock the trigger every 60 seconds for simplicity in this implementation.
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void processScheduledTasks() {
        List<ScheduledTask> tasks = scheduledTaskRepository.findByActiveTrue();
        for (ScheduledTask task : tasks) {
            // Context needs to be set for background execution
            TenantContext.set(task.getTenantId());
            try {
                JobRequest req = new JobRequest();
                req.setName(task.getJobName());
                req.setPayload(task.getPayloadTemplate());
                req.setPriority(task.getPriority());
                jobService.enqueueJob(req);
            } finally {
                TenantContext.clear();
            }
        }
    }
}
