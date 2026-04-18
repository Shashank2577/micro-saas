package com.microsaas.taskqueue.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.taskqueue.domain.Job;
import com.microsaas.taskqueue.domain.JobHistory;
import com.microsaas.taskqueue.domain.JobStatus;
import com.microsaas.taskqueue.dto.JobRequest;
import com.microsaas.taskqueue.repository.JobHistoryRepository;
import com.microsaas.taskqueue.repository.JobRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTest {

    private JobRepository jobRepository;
    private JobHistoryRepository jobHistoryRepository;
    private JobService jobService;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        jobRepository = mock(JobRepository.class);
        jobHistoryRepository = mock(JobHistoryRepository.class);
        jobService = new JobService(jobRepository, jobHistoryRepository);
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testEnqueueJob() {
        JobRequest request = new JobRequest();
        request.setName("test-job");
        request.setPayload("{\"key\":\"value\"}");

        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Job job = jobService.enqueueJob(request);

        assertNotNull(job);
        assertEquals("test-job", job.getName());
        assertEquals(tenantId, job.getTenantId());
        assertEquals(JobStatus.PENDING, job.getStatus());
        assertEquals("{\"key\":\"value\"}", job.getPayload());

        verify(jobHistoryRepository).save(any(JobHistory.class));
    }

    @Test
    void testCancelJob() {
        Job job = new Job();
        job.setId(UUID.randomUUID());
        job.setTenantId(tenantId);
        job.setStatus(JobStatus.PENDING);

        when(jobRepository.findById(job.getId())).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Job cancelledJob = jobService.cancelJob(job.getId());

        assertEquals(JobStatus.CANCELLED, cancelledJob.getStatus());
        verify(jobHistoryRepository).save(any(JobHistory.class));
    }

    @Test
    void testRetryJob() {
        Job job = new Job();
        job.setId(UUID.randomUUID());
        job.setTenantId(tenantId);
        job.setStatus(JobStatus.FAILED);
        job.setRetryCount(3);

        when(jobRepository.findById(job.getId())).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Job retriedJob = jobService.retryJob(job.getId());

        assertEquals(JobStatus.PENDING, retriedJob.getStatus());
        assertEquals(0, retriedJob.getRetryCount());
        assertNotNull(retriedJob.getNextRunAt());
        verify(jobHistoryRepository).save(any(JobHistory.class));
    }
}
