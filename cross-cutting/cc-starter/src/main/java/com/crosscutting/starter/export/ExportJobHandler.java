package com.crosscutting.starter.export;

import com.crosscutting.starter.queue.Job;
import com.crosscutting.starter.queue.JobHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Processes export jobs from the queue. Picks up pending export jobs,
 * marks them as processing, and transitions to completed/failed.
 *
 * The actual data export (CSV/JSON generation, S3 upload) is left as a
 * hook for consumer applications to extend. This handler provides the
 * lifecycle management.
 */
@Component
public class ExportJobHandler implements JobHandler {

    private static final Logger log = LoggerFactory.getLogger(ExportJobHandler.class);
    private static final String QUEUE_NAME = "exports";

    private final ExportJobRepository exportJobRepository;
    private final ObjectMapper objectMapper;

    public ExportJobHandler(ExportJobRepository exportJobRepository, ObjectMapper objectMapper) {
        this.exportJobRepository = exportJobRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getQueueName() {
        return QUEUE_NAME;
    }

    @Override
    public void handle(Job job) {
        try {
            JsonNode payload = objectMapper.readTree(job.getPayload());
            UUID exportJobId = UUID.fromString(payload.get("exportJobId").asText());

            ExportJob exportJob = exportJobRepository.findById(exportJobId)
                    .orElseThrow(() -> new IllegalStateException("Export job not found: " + exportJobId));

            // Mark as processing
            exportJob.setStatus("processing");
            exportJobRepository.save(exportJob);

            log.info("Processing export job {} (resource={}, format={})",
                    exportJobId, exportJob.getResourceType(), exportJob.getFormat());

            // TODO: Implement actual export logic per resource type.
            // Consumer applications should override or extend this handler
            // to generate CSV/JSON files and upload to storage.
            // For now, mark as completed to unblock the pipeline.
            exportJob.setStatus("completed");
            exportJob.setCompletedAt(Instant.now());
            exportJobRepository.save(exportJob);

            log.info("Export job {} completed", exportJobId);

        } catch (Exception e) {
            log.error("Export job failed for queue job {}: {}", job.getId(), e.getMessage(), e);
            // Mark the export job as failed so users see the error via status polling
            try {
                JsonNode payload = objectMapper.readTree(job.getPayload());
                if (payload.has("exportJobId")) {
                    UUID failedJobId = UUID.fromString(payload.get("exportJobId").asText());
                    exportJobRepository.findById(failedJobId).ifPresent(ej -> {
                        ej.setStatus("failed");
                        ej.setErrorMessage(e.getMessage());
                        exportJobRepository.save(ej);
                    });
                }
            } catch (Exception inner) {
                log.error("Failed to mark export job as failed: {}", inner.getMessage());
            }
            throw new RuntimeException("Export job processing failed", e);
        }
    }
}
