package com.crosscutting.starter.export;

import com.crosscutting.starter.queue.Job;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportJobHandlerTest {

    @Mock
    private ExportJobRepository exportJobRepository;

    private ExportJobHandler handler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        handler = new ExportJobHandler(exportJobRepository, objectMapper);
    }

    @Test
    void getQueueName_returnsExports() {
        assertThat(handler.getQueueName()).isEqualTo("exports");
    }

    @Test
    void handle_processesAndCompletesExportJob() {
        UUID exportJobId = UUID.randomUUID();
        ExportJob exportJob = new ExportJob();
        exportJob.setId(exportJobId);
        exportJob.setResourceType("users");
        exportJob.setFormat("CSV");
        exportJob.setStatus("pending");

        Job queueJob = new Job();
        queueJob.setId(1L);
        queueJob.setPayload(String.format("{\"exportJobId\":\"%s\"}", exportJobId));

        when(exportJobRepository.findById(exportJobId)).thenReturn(Optional.of(exportJob));

        // Track status transitions through save calls
        java.util.List<String> statusTransitions = new java.util.ArrayList<>();
        when(exportJobRepository.save(any(ExportJob.class))).thenAnswer(inv -> {
            ExportJob saved = inv.getArgument(0);
            statusTransitions.add(saved.getStatus());
            return saved;
        });

        handler.handle(queueJob);

        verify(exportJobRepository, times(2)).save(any(ExportJob.class));
        assertThat(statusTransitions).containsExactly("processing", "completed");
        assertThat(exportJob.getCompletedAt()).isNotNull();
    }

    @Test
    void handle_throwsWhenExportJobNotFound_andMarksJobAsFailed() {
        UUID missingId = UUID.randomUUID();
        Job queueJob = new Job();
        queueJob.setId(1L);
        queueJob.setPayload(String.format("{\"exportJobId\":\"%s\"}", missingId));

        when(exportJobRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(queueJob))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Export job processing failed")
                .hasCauseInstanceOf(IllegalStateException.class);
    }

    @Test
    void handle_marksExportJobAsFailedOnError() {
        UUID exportJobId = UUID.randomUUID();
        ExportJob exportJob = new ExportJob();
        exportJob.setId(exportJobId);
        exportJob.setResourceType("users");
        exportJob.setFormat("CSV");
        exportJob.setStatus("pending");

        Job queueJob = new Job();
        queueJob.setId(1L);
        queueJob.setPayload(String.format("{\"exportJobId\":\"%s\"}", exportJobId));

        // First findById returns the job, second (in catch block) also returns it
        when(exportJobRepository.findById(exportJobId)).thenReturn(Optional.of(exportJob));

        // First save (processing) succeeds, then throw to simulate failure
        java.util.concurrent.atomic.AtomicInteger saveCount = new java.util.concurrent.atomic.AtomicInteger();
        when(exportJobRepository.save(any(ExportJob.class))).thenAnswer(inv -> {
            if (saveCount.incrementAndGet() == 1) {
                // After marking as processing, throw to simulate an error
                throw new RuntimeException("Simulated storage failure");
            }
            return inv.getArgument(0);
        });

        assertThatThrownBy(() -> handler.handle(queueJob))
                .isInstanceOf(RuntimeException.class);

        // The catch block should have tried to mark the job as failed
        assertThat(exportJob.getStatus()).isEqualTo("failed");
        assertThat(exportJob.getErrorMessage()).isNotNull();
    }

    @Test
    void handle_throwsOnInvalidPayload() {
        Job queueJob = new Job();
        queueJob.setId(1L);
        queueJob.setPayload("not-valid-json");

        assertThatThrownBy(() -> handler.handle(queueJob))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(com.fasterxml.jackson.core.JsonProcessingException.class);
    }

    @Test
    void handle_throwsOnMissingExportJobIdField() {
        Job queueJob = new Job();
        queueJob.setId(1L);
        queueJob.setPayload("{\"someField\":\"value\"}");

        assertThatThrownBy(() -> handler.handle(queueJob))
                .isInstanceOf(RuntimeException.class);
    }
}
