package com.microsaas.taskqueue.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.taskqueue.domain.JobStatus;
import com.microsaas.taskqueue.dto.DashboardStats;
import com.microsaas.taskqueue.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final JobRepository jobRepository;

    public DashboardController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        UUID tenantId = TenantContext.require();
        DashboardStats stats = new DashboardStats();
        
        long total = jobRepository.countByTenantId(tenantId);
        long completed = jobRepository.countByTenantIdAndStatus(tenantId, JobStatus.COMPLETED);
        long failed = jobRepository.countByTenantIdAndStatus(tenantId, JobStatus.FAILED);
        
        stats.setTotalJobs(total);
        stats.setPendingJobs(jobRepository.countByTenantIdAndStatus(tenantId, JobStatus.PENDING));
        stats.setRunningJobs(jobRepository.countByTenantIdAndStatus(tenantId, JobStatus.RUNNING));
        stats.setCompletedJobs(completed);
        stats.setFailedJobs(failed);
        stats.setDeadLetterJobs(jobRepository.countByTenantIdAndStatus(tenantId, JobStatus.DEAD_LETTER));
        
        long finished = completed + failed;
        if (finished > 0) {
            stats.setSuccessRate((double) completed / finished * 100);
        } else {
            stats.setSuccessRate(100.0);
        }
        
        return ResponseEntity.ok(stats);
    }
}
