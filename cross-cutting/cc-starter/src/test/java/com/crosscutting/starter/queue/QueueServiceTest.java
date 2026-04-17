package com.crosscutting.starter.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private QueueService queueService;

    @BeforeEach
    void setUp() {
        queueService = new QueueService(jdbcTemplate);
    }

    @Test
    void enqueueInsertsJobIntoTable() {
        queueService.enqueue("cc_jobs", "{\"type\":\"email\"}", 0);

        verify(jdbcTemplate).update(
                contains("INSERT INTO cc.job_queue"),
                eq("cc_jobs"),
                eq("{\"type\":\"email\"}"),
                eq("0")
        );
    }

    @Test
    void enqueueWithDelaySetsScheduledAt() {
        queueService.enqueue("cc_exports", "{\"exportId\":1}", 60);

        verify(jdbcTemplate).update(
                contains("INSERT INTO cc.job_queue"),
                eq("cc_exports"),
                eq("{\"exportId\":1}"),
                eq("60")
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void dequeueReturnsJobWhenAvailable() {
        Job job = new Job(1L, "cc_jobs", "{}", 1, 3,
                java.time.Instant.now(), java.time.Instant.now());

        when(jdbcTemplate.query(contains("UPDATE cc.job_queue"), any(RowMapper.class), eq("cc_jobs")))
                .thenReturn(List.of(job));

        Optional<Job> result = queueService.dequeue("cc_jobs");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("cc_jobs", result.get().getQueueName());
    }

    @Test
    @SuppressWarnings("unchecked")
    void dequeueReturnsEmptyWhenNoJobsAvailable() {
        when(jdbcTemplate.query(contains("UPDATE cc.job_queue"), any(RowMapper.class), eq("cc_jobs")))
                .thenReturn(Collections.emptyList());

        Optional<Job> result = queueService.dequeue("cc_jobs");

        assertTrue(result.isEmpty());
    }

    @Test
    void markCompleteUpdatesStatus() {
        queueService.markComplete(42L);

        verify(jdbcTemplate).update(
                contains("status = 'completed'"),
                eq(42L)
        );
    }

    @Test
    void markFailedUpdatesStatusAndError() {
        queueService.markFailed(42L, "Connection timeout");

        verify(jdbcTemplate).update(
                contains("status = 'failed'"),
                eq("Connection timeout"),
                eq(42L)
        );
    }

    @Test
    void moveToDeadLetterUpdatesStatus() {
        queueService.moveToDeadLetter(42L);

        verify(jdbcTemplate).update(
                contains("status = 'dead_letter'"),
                eq(42L)
        );
    }

    @Test
    void markRetryUpdatesStatus() {
        queueService.markRetry(42L);

        verify(jdbcTemplate).update(
                contains("status = 'retry'"),
                eq(42L)
        );
    }

    @Test
    void getQueueStatsQueriesGroupedCounts() {
        List<Map<String, Object>> stats = List.of(
                Map.of("queue_name", "cc_jobs", "status", "pending", "count", 5L)
        );
        when(jdbcTemplate.queryForList(contains("GROUP BY"))).thenReturn(stats);

        List<Map<String, Object>> result = queueService.getQueueStats();

        assertEquals(1, result.size());
        assertEquals("cc_jobs", result.get(0).get("queue_name"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void getDeadLetterJobsQueriesByQueueName() {
        Job deadJob = new Job(99L, "cc_jobs", "{}", 5, 5,
                java.time.Instant.now(), java.time.Instant.now());
        when(jdbcTemplate.query(contains("dead_letter"), any(RowMapper.class), eq("cc_jobs")))
                .thenReturn(List.of(deadJob));

        List<Job> result = queueService.getDeadLetterJobs("cc_jobs");

        assertEquals(1, result.size());
        assertEquals(99L, result.get(0).getId());
    }

    @Test
    void retryDeadLetterJobResetsStatus() {
        queueService.retryDeadLetterJob(99L);

        verify(jdbcTemplate).update(
                contains("status = 'pending'"),
                eq(99L)
        );
    }
}
