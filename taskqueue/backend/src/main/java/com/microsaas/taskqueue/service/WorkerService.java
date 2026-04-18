package com.microsaas.taskqueue.service;

import com.microsaas.taskqueue.domain.Job;
import com.microsaas.taskqueue.domain.JobHistory;
import com.microsaas.taskqueue.domain.JobStatus;
import com.microsaas.taskqueue.repository.JobHistoryRepository;
import com.microsaas.taskqueue.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service
public class WorkerService {
    private static final Logger log = LoggerFactory.getLogger(WorkerService.class);
    private final JobRepository jobRepository;
    private final JobHistoryRepository jobHistoryRepository;
    private final ExecutorService executor;
    private final Semaphore concurrencySemaphore;
    private final EventPublisherService eventPublisherService;

    public WorkerService(JobRepository jobRepository, JobHistoryRepository jobHistoryRepository, EventPublisherService eventPublisherService) {
        this.jobRepository = jobRepository;
        this.jobHistoryRepository = jobHistoryRepository;
        this.eventPublisherService = eventPublisherService;
        this.executor = Executors.newFixedThreadPool(10);
        this.concurrencySemaphore = new Semaphore(50); // Max 50 concurrent jobs
    }

    @Scheduled(fixedDelay = 1000)
    public void pollJobs() {
        if (concurrencySemaphore.availablePermits() == 0) {
            return;
        }

        List<Job> readyJobs = jobRepository.findReadyJobs(JobStatus.PENDING, Instant.now());
        for (Job job : readyJobs) {
            if (job.getDependsOnJobId() != null) {
                Job parent = jobRepository.findById(job.getDependsOnJobId()).orElse(null);
                if (parent == null) { continue; }
                if (parent.getStatus() == JobStatus.FAILED || parent.getStatus() == JobStatus.DEAD_LETTER || parent.getStatus() == JobStatus.CANCELLED) {
                    job.setStatus(JobStatus.FAILED);
                    job.setResult("{\"error\": \"Parent job failed or cancelled\"}");
                    jobRepository.save(job);
                    recordHistory(job, "Job failed because parent failed");
                    continue;
                }
                if (parent.getStatus() != JobStatus.COMPLETED) {
                    continue; // Skip until parent completes
                }
            }

            if (concurrencySemaphore.tryAcquire()) {
                startJob(job.getId());
            } else {
                break;
            }
        }
    }

    @Transactional
    public void startJob(UUID jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null || job.getStatus() != JobStatus.PENDING) {
            concurrencySemaphore.release();
            return;
        }
        
        job.setStatus(JobStatus.RUNNING);
        job.setStartedAt(Instant.now());
        jobRepository.save(job);
        recordHistory(job, "Job started");

        executor.submit(() -> executeJob(job.getId()));
    }

    private void executeJob(UUID jobId) {
        try {
            Job job = jobRepository.findById(jobId).orElseThrow();
            
            // Mock execution logic
            Thread.sleep(100);
            
            if ("fail_test".equals(job.getName()) && job.getRetryCount() < 2) {
                throw new RuntimeException("Simulated failure");
            }
            if ("fail_dlq".equals(job.getName())) {
                throw new RuntimeException("Simulated terminal failure");
            }

            handleJobSuccess(jobId, "{\"result\": \"success\"}");
        } catch (Exception e) {
            handleJobFailure(jobId, e);
        } finally {
            concurrencySemaphore.release();
        }
    }

    @Transactional
    public void handleJobSuccess(UUID jobId, String result) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        job.setStatus(JobStatus.COMPLETED);
        job.setCompletedAt(Instant.now());
        job.setResult(result);
        jobRepository.save(job);
        recordHistory(job, "Job completed successfully");
        eventPublisherService.publishJobCompleted(job.getId(), job.getName(), job.getStatus().name(), job.getResult());
    }

    @Transactional
    public void handleJobFailure(UUID jobId, Exception e) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        int newRetryCount = job.getRetryCount() + 1;
        
        if (newRetryCount > job.getMaxRetries()) {
            job.setStatus(JobStatus.DEAD_LETTER);
            job.setResult("{\"error\": \"" + e.getMessage() + "\"}");
            jobRepository.save(job);
            recordHistory(job, "Max retries exceeded. Moved to DLQ. Error: " + e.getMessage());
            eventPublisherService.publishJobFailed(job.getId(), job.getName(), job.getStatus().name(), e.getMessage());
        } else {
            job.setStatus(JobStatus.PENDING);
            job.setRetryCount(newRetryCount);
            // Exponential backoff
            long backoffSeconds = (long) Math.pow(2, newRetryCount);
            job.setNextRunAt(Instant.now().plusSeconds(backoffSeconds));
            jobRepository.save(job);
            recordHistory(job, "Job failed. Scheduled for retry " + newRetryCount + ". Error: " + e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void handleTimeouts() {
        Instant now = Instant.now();
        List<Job> runningJobs = jobRepository.findTimedOutJobs(now);
        for (Job job : runningJobs) {
            if (job.getStartedAt() != null && 
                job.getStartedAt().plusSeconds(job.getTimeoutSeconds()).isBefore(now)) {
                job.setStatus(JobStatus.FAILED);
                job.setResult("{\"error\": \"Job timed out\"}");
                jobRepository.save(job);
                recordHistory(job, "Job timed out after " + job.getTimeoutSeconds() + " seconds");
            }
        }
    }

    private void recordHistory(Job job, String message) {
        JobHistory history = new JobHistory();
        history.setJobId(job.getId());
        history.setStatus(job.getStatus());
        history.setMessage(message);
        jobHistoryRepository.save(history);
    }
}
