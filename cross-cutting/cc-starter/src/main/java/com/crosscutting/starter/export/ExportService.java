package com.crosscutting.starter.export;

import com.crosscutting.starter.error.CcErrorCodes;
import com.crosscutting.starter.queue.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ExportService {

    private static final Logger log = LoggerFactory.getLogger(ExportService.class);

    private final ExportJobRepository exportJobRepository;
    private final QueueService queueService;

    public ExportService(ExportJobRepository exportJobRepository, QueueService queueService) {
        this.exportJobRepository = exportJobRepository;
        this.queueService = queueService;
    }

    /**
     * Start an export job. Creates a pending job and returns its ID.
     */
    @Transactional
    public UUID startExport(UUID tenantId, String resourceType, String format, String query, String columns) {
        ExportJob job = new ExportJob();
        job.setTenantId(tenantId);
        job.setResourceType(resourceType);
        job.setStatus("pending");
        job.setFormat(format);
        job.setQuery(query);
        job.setColumns(columns);
        job = exportJobRepository.save(job);

        // Enqueue the job for async processing
        queueService.enqueue("exports",
                String.format("{\"exportJobId\":\"%s\"}", job.getId()), 0);

        log.info("Started export job {} for tenant {} (resource={}, format={})",
                job.getId(), tenantId, resourceType, format);
        return job.getId();
    }

    /**
     * Get the status of a job by ID.
     */
    public ExportJob getJobStatus(UUID jobId) {
        return exportJobRepository.findById(jobId)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Export job not found: " + jobId));
    }

    /**
     * Start an import job. Creates a pending import job and returns its ID.
     */
    @Transactional
    public UUID startImport(UUID tenantId, String resourceType, String fileKey) {
        ExportJob job = new ExportJob();
        job.setTenantId(tenantId);
        job.setResourceType(resourceType);
        job.setStatus("pending");
        job.setFormat("import");
        job.setResultUrl(fileKey);
        job = exportJobRepository.save(job);

        // Enqueue the import job for async processing
        queueService.enqueue("exports",
                String.format("{\"exportJobId\":\"%s\"}", job.getId()), 0);

        log.info("Started import job {} for tenant {} (resource={}, fileKey={})",
                job.getId(), tenantId, resourceType, fileKey);
        return job.getId();
    }

    /**
     * List all export/import jobs for a tenant.
     */
    public Page<ExportJob> listJobs(UUID tenantId, Pageable pageable) {
        return exportJobRepository.findByTenantId(tenantId, pageable);
    }
}
