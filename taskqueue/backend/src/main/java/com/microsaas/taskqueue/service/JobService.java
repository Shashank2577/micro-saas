package com.microsaas.taskqueue.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.taskqueue.domain.Job;
import com.microsaas.taskqueue.domain.JobHistory;
import com.microsaas.taskqueue.domain.JobStatus;
import com.microsaas.taskqueue.dto.JobRequest;
import com.microsaas.taskqueue.repository.JobHistoryRepository;
import com.microsaas.taskqueue.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final JobHistoryRepository jobHistoryRepository;

    public JobService(JobRepository jobRepository, JobHistoryRepository jobHistoryRepository) {
        this.jobRepository = jobRepository;
        this.jobHistoryRepository = jobHistoryRepository;
    }

    @Transactional
    public Job enqueueJob(JobRequest request) {
        UUID tenantId = TenantContext.require();
        
        if (request.getDependsOnJobId() != null) {
            Job parentJob = jobRepository.findById(request.getDependsOnJobId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent job not found"));
            if (!parentJob.getTenantId().equals(tenantId)) {
                throw new IllegalArgumentException("Parent job not found");
            }
        }

        Job job = new Job();
        job.setTenantId(tenantId);
        job.setName(request.getName());
        job.setPriority(request.getPriority());
        job.setStatus(JobStatus.PENDING);
        job.setPayload(request.getPayload());
        job.setMaxRetries(request.getMaxRetries() != null ? request.getMaxRetries() : 3);
        job.setTimeoutSeconds(request.getTimeoutSeconds() != null ? request.getTimeoutSeconds() : 3600);
        job.setDependsOnJobId(request.getDependsOnJobId());
        
        Job savedJob = jobRepository.save(job);
        recordHistory(savedJob, "Job enqueued");
        return savedJob;
    }

    @Transactional
    public List<Job> enqueueBatch(List<JobRequest> requests) {
        return requests.stream().map(this::enqueueJob).toList();
    }

    public List<Job> getJobs(JobStatus status) {
        UUID tenantId = TenantContext.require();
        if (status != null) {
            return jobRepository.findByTenantIdAndStatus(tenantId, status);
        }
        return jobRepository.findByTenantId(tenantId);
    }

    public Job getJob(UUID id) {
        UUID tenantId = TenantContext.require();
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        if (!job.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Job not found");
        }
        return job;
    }

    @Transactional
    public Job cancelJob(UUID id) {
        Job job = getJob(id);
        if (job.getStatus() == JobStatus.COMPLETED || job.getStatus() == JobStatus.CANCELLED) {
            return job;
        }
        job.setStatus(JobStatus.CANCELLED);
        Job saved = jobRepository.save(job);
        recordHistory(saved, "Job cancelled by user");
        return saved;
    }

    @Transactional
    public Job retryJob(UUID id) {
        Job job = getJob(id);
        if (job.getStatus() != JobStatus.DEAD_LETTER && job.getStatus() != JobStatus.FAILED) {
            throw new IllegalStateException("Can only retry failed or dead-letter jobs");
        }
        job.setStatus(JobStatus.PENDING);
        job.setRetryCount(0);
        job.setNextRunAt(Instant.now());
        job.setResult(null);
        Job saved = jobRepository.save(job);
        recordHistory(saved, "Job manually retried");
        return saved;
    }
    
    public List<JobHistory> getJobHistory(UUID id) {
        Job job = getJob(id);
        return jobHistoryRepository.findByJobIdOrderByCreatedAtDesc(job.getId());
    }

    private void recordHistory(Job job, String message) {
        JobHistory history = new JobHistory();
        history.setJobId(job.getId());
        history.setStatus(job.getStatus());
        history.setMessage(message);
        jobHistoryRepository.save(history);
    }
}
