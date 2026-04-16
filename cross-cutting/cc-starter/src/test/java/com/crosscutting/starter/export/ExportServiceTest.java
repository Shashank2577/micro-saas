package com.crosscutting.starter.export;

import com.crosscutting.starter.error.CcException;
import com.crosscutting.starter.queue.QueueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

    @Mock
    private ExportJobRepository exportJobRepository;

    @Mock
    private QueueService queueService;

    @InjectMocks
    private ExportService exportService;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void startExport_createsJobWithPendingStatus() {
        ExportJob saved = new ExportJob();
        saved.setId(UUID.randomUUID());
        saved.setTenantId(tenantId);
        saved.setStatus("pending");
        saved.setFormat("csv");
        saved.setResourceType("users");

        when(exportJobRepository.save(any(ExportJob.class))).thenReturn(saved);

        UUID jobId = exportService.startExport(tenantId, "users", "csv", "active=true", "name,email");

        assertThat(jobId).isEqualTo(saved.getId());

        ArgumentCaptor<ExportJob> captor = ArgumentCaptor.forClass(ExportJob.class);
        verify(exportJobRepository).save(captor.capture());
        ExportJob captured = captor.getValue();
        assertThat(captured.getTenantId()).isEqualTo(tenantId);
        assertThat(captured.getResourceType()).isEqualTo("users");
        assertThat(captured.getStatus()).isEqualTo("pending");
        assertThat(captured.getFormat()).isEqualTo("csv");
        assertThat(captured.getQuery()).isEqualTo("active=true");
        assertThat(captured.getColumns()).isEqualTo("name,email");

        // Verify job was enqueued
        verify(queueService).enqueue(eq("exports"), anyString(), eq(0));
    }

    @Test
    void getJobStatus_returnsJobWhenFound() {
        UUID jobId = UUID.randomUUID();
        ExportJob job = new ExportJob();
        job.setId(jobId);
        job.setStatus("completed");

        when(exportJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        ExportJob result = exportService.getJobStatus(jobId);

        assertThat(result.getId()).isEqualTo(jobId);
        assertThat(result.getStatus()).isEqualTo("completed");
    }

    @Test
    void getJobStatus_throwsWhenNotFound() {
        UUID jobId = UUID.randomUUID();
        when(exportJobRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> exportService.getJobStatus(jobId))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "RESOURCE_NOT_FOUND");
    }

    @Test
    void startImport_createsImportJobWithPendingStatus() {
        ExportJob saved = new ExportJob();
        saved.setId(UUID.randomUUID());
        saved.setTenantId(tenantId);
        saved.setStatus("pending");
        saved.setFormat("import");
        saved.setResourceType("users");

        when(exportJobRepository.save(any(ExportJob.class))).thenReturn(saved);

        UUID jobId = exportService.startImport(tenantId, "users", "uploads/file.csv");

        assertThat(jobId).isEqualTo(saved.getId());

        ArgumentCaptor<ExportJob> captor = ArgumentCaptor.forClass(ExportJob.class);
        verify(exportJobRepository).save(captor.capture());
        ExportJob captured = captor.getValue();
        assertThat(captured.getTenantId()).isEqualTo(tenantId);
        assertThat(captured.getResourceType()).isEqualTo("users");
        assertThat(captured.getStatus()).isEqualTo("pending");
        assertThat(captured.getFormat()).isEqualTo("import");
        assertThat(captured.getResultUrl()).isEqualTo("uploads/file.csv");

        // Verify import job was enqueued
        verify(queueService).enqueue(eq("exports"), anyString(), eq(0));
    }

    @Test
    void listJobs_returnsPagedResults() {
        ExportJob job = new ExportJob();
        job.setId(UUID.randomUUID());
        job.setTenantId(tenantId);
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExportJob> page = new PageImpl<>(List.of(job), pageable, 1);

        when(exportJobRepository.findByTenantId(tenantId, pageable)).thenReturn(page);

        Page<ExportJob> result = exportService.listJobs(tenantId, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTenantId()).isEqualTo(tenantId);
    }
}
