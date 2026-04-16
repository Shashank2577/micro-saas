package com.crosscutting.starter.export;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for ExportJob entities. Sanitizes the errorMessage field
 * to avoid leaking internal stack traces or implementation details to clients.
 */
public record ExportJobResponse(
        UUID id,
        UUID tenantId,
        String resourceType,
        String status,
        String format,
        String query,
        String columns,
        String resultUrl,
        String errorMessage,
        Instant createdAt,
        Instant completedAt
) {
    public static ExportJobResponse from(ExportJob job) {
        // Sanitize error message — return a generic message if the job failed,
        // keeping the full error in server logs only
        String sanitizedError = null;
        if (job.getErrorMessage() != null) {
            sanitizedError = "Export failed. Contact support with job ID: " + job.getId();
        }

        return new ExportJobResponse(
                job.getId(),
                job.getTenantId(),
                job.getResourceType(),
                job.getStatus(),
                job.getFormat(),
                job.getQuery(),
                job.getColumns(),
                job.getResultUrl(),
                sanitizedError,
                job.getCreatedAt(),
                job.getCompletedAt()
        );
    }
}
