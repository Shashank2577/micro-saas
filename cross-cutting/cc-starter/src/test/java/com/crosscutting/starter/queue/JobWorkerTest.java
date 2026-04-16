package com.crosscutting.starter.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobWorkerTest {

    @Mock
    private QueueService queueService;

    @Mock
    private JobHandler exportHandler;

    @Mock
    private JobHandler emailHandler;

    private JobWorker jobWorker;

    @BeforeEach
    void setUp() {
        when(exportHandler.getQueueName()).thenReturn("exports");
        when(emailHandler.getQueueName()).thenReturn("emails");
        jobWorker = new JobWorker(queueService, List.of(exportHandler, emailHandler));
    }

    @Test
    void poll_processesJobFromEachQueue() {
        Job exportJob = new Job(1L, "exports", "{}", 1, 3, Instant.now(), Instant.now());
        Job emailJob = new Job(2L, "emails", "{}", 1, 3, Instant.now(), Instant.now());

        when(queueService.dequeue("exports")).thenReturn(Optional.of(exportJob));
        when(queueService.dequeue("emails")).thenReturn(Optional.of(emailJob));

        jobWorker.poll();

        verify(exportHandler).handle(exportJob);
        verify(emailHandler).handle(emailJob);
        verify(queueService).markComplete(1L);
        verify(queueService).markComplete(2L);
    }

    @Test
    void poll_skipsEmptyQueues() {
        when(queueService.dequeue("exports")).thenReturn(Optional.empty());
        when(queueService.dequeue("emails")).thenReturn(Optional.empty());

        jobWorker.poll();

        verify(exportHandler, never()).handle(any());
        verify(emailHandler, never()).handle(any());
        verify(queueService, never()).markComplete(anyLong());
    }

    @Test
    void poll_retriesOnFailureBelowMaxAttempts() {
        Job job = new Job(1L, "exports", "{}", 1, 3, Instant.now(), Instant.now());
        when(queueService.dequeue("exports")).thenReturn(Optional.of(job));
        when(queueService.dequeue("emails")).thenReturn(Optional.empty());
        doThrow(new RuntimeException("fail")).when(exportHandler).handle(job);

        jobWorker.poll();

        verify(queueService).markRetry(1L);
        verify(queueService, never()).moveToDeadLetter(1L);
    }

    @Test
    void poll_movesToDeadLetterOnMaxAttempts() {
        Job job = new Job(1L, "exports", "{}", 3, 3, Instant.now(), Instant.now());
        when(queueService.dequeue("exports")).thenReturn(Optional.of(job));
        when(queueService.dequeue("emails")).thenReturn(Optional.empty());
        doThrow(new RuntimeException("fail")).when(exportHandler).handle(job);

        jobWorker.poll();

        verify(queueService).moveToDeadLetter(1L);
        verify(queueService, never()).markRetry(1L);
    }

    @Test
    void poll_usesDefaultMaxAttemptsWhenZero() {
        // maxAttempts=0 should use default of 3
        Job job = new Job(1L, "exports", "{}", 3, 0, Instant.now(), Instant.now());
        when(queueService.dequeue("exports")).thenReturn(Optional.of(job));
        when(queueService.dequeue("emails")).thenReturn(Optional.empty());
        doThrow(new RuntimeException("fail")).when(exportHandler).handle(job);

        jobWorker.poll();

        verify(queueService).moveToDeadLetter(1L);
    }
}
