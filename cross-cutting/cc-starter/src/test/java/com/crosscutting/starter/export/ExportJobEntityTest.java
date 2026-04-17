package com.crosscutting.starter.export;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExportJobEntityTest {

    @Test
    void defaultStatusIsPending() {
        ExportJob job = new ExportJob();
        assertThat(job.getStatus()).isEqualTo("pending");
    }

    @Test
    void defaultFormatIsCSV() {
        ExportJob job = new ExportJob();
        assertThat(job.getFormat()).isEqualTo("CSV");
    }

    @Test
    void allFieldsAreSettable() {
        ExportJob job = new ExportJob();
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        Instant now = Instant.now();

        job.setId(id);
        job.setTenantId(tenantId);
        job.setResourceType("users");
        job.setStatus("completed");
        job.setFormat("JSON");
        job.setQuery("status=active");
        job.setColumns("name,email");
        job.setResultUrl("s3://bucket/export.csv");
        job.setErrorMessage("some error");
        job.setCompletedAt(now);

        assertThat(job.getId()).isEqualTo(id);
        assertThat(job.getTenantId()).isEqualTo(tenantId);
        assertThat(job.getResourceType()).isEqualTo("users");
        assertThat(job.getStatus()).isEqualTo("completed");
        assertThat(job.getFormat()).isEqualTo("JSON");
        assertThat(job.getQuery()).isEqualTo("status=active");
        assertThat(job.getColumns()).isEqualTo("name,email");
        assertThat(job.getResultUrl()).isEqualTo("s3://bucket/export.csv");
        assertThat(job.getErrorMessage()).isEqualTo("some error");
        assertThat(job.getCompletedAt()).isEqualTo(now);
    }
}
