package com.crosscutting.starter.export;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cc/export")
@Tag(name = "Export / Import", description = "Async data export and import job management")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * Start an export job.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Start an export job", description = "Queue an async data export for the specified tenant, resource type, and format")
    @ApiResponse(responseCode = "201", description = "Export job started")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Map<String, UUID> startExport(@Valid @RequestBody StartExportRequest request) {
        UUID jobId = exportService.startExport(
                request.tenantId(), request.resourceType(), request.format(),
                request.query(), request.columns());
        return Map.of("jobId", jobId);
    }

    /**
     * Get job status and download URL.
     */
    @GetMapping("/{jobId}")
    @Operation(summary = "Get export job status", description = "Retrieve the current status and download URL for an export job")
    @ApiResponse(responseCode = "200", description = "Job status returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Job not found")
    public ExportJobResponse getJobStatus(@Parameter(description = "Export job ID") @PathVariable UUID jobId) {
        return ExportJobResponse.from(exportService.getJobStatus(jobId));
    }

    /**
     * Start an import job.
     */
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Start an import job", description = "Queue an async data import from an uploaded file for the specified tenant and resource type")
    @ApiResponse(responseCode = "201", description = "Import job started")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Map<String, UUID> startImport(@Valid @RequestBody StartImportRequest request) {
        UUID jobId = exportService.startImport(
                request.tenantId(), request.resourceType(), request.fileKey());
        return Map.of("jobId", jobId);
    }

    /**
     * List all export/import jobs for a tenant.
     */
    @GetMapping("/jobs")
    @Operation(summary = "List export/import jobs", description = "Retrieve a paginated list of all export and import jobs for a tenant")
    @ApiResponse(responseCode = "200", description = "Jobs listed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Page<ExportJobResponse> listJobs(@Parameter(description = "Tenant ID") @RequestParam UUID tenantId, Pageable pageable) {
        return exportService.listJobs(tenantId, pageable).map(ExportJobResponse::from);
    }

    public record StartExportRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "Resource type is required") String resourceType,
            @NotBlank(message = "Format is required") String format,
            String query,
            String columns) {
    }

    public record StartImportRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "Resource type is required") String resourceType,
            @NotBlank(message = "File key is required") String fileKey) {
    }
}
