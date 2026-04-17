package com.crosscutting.starter.export;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExportJobResponseTest {

    @Test
    void from_mapsAllFieldsFromEntity() {
        ExportJob job = new ExportJob();
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        Instant now = Instant.now();

        job.setId(id);
        job.setTenantId(tenantId);
        job.setResourceType("users");
        job.setStatus("completed");
        job.setFormat("CSV");
        job.setQuery("active=true");
        job.setColumns("name,email");
        job.setResultUrl("https://storage/export.csv");
        job.setCompletedAt(now);

        ExportJobResponse response = ExportJobResponse.from(job);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.tenantId()).isEqualTo(tenantId);
        assertThat(response.resourceType()).isEqualTo("users");
        assertThat(response.status()).isEqualTo("completed");
        assertThat(response.format()).isEqualTo("CSV");
        assertThat(response.query()).isEqualTo("active=true");
        assertThat(response.columns()).isEqualTo("name,email");
        assertThat(response.resultUrl()).isEqualTo("https://storage/export.csv");
        assertThat(response.errorMessage()).isNull();
        assertThat(response.completedAt()).isEqualTo(now);
    }

    @Test
    void from_sanitizesErrorMessage() {
        ExportJob job = new ExportJob();
        UUID id = UUID.randomUUID();
        job.setId(id);
        job.setTenantId(UUID.randomUUID());
        job.setResourceType("orders");
        job.setStatus("failed");
        job.setFormat("JSON");
        job.setErrorMessage("java.lang.NullPointerException: secret internal detail at com.foo.Bar");

        ExportJobResponse response = ExportJobResponse.from(job);

        assertThat(response.errorMessage()).startsWith("Export failed. Contact support with job ID:");
        assertThat(response.errorMessage()).contains(id.toString());
        assertThat(response.errorMessage()).doesNotContain("NullPointerException");
    }

    @Test
    void from_returnsNullErrorWhenNoError() {
        ExportJob job = new ExportJob();
        job.setId(UUID.randomUUID());
        job.setTenantId(UUID.randomUUID());
        job.setResourceType("users");
        job.setStatus("completed");
        job.setFormat("CSV");
        job.setErrorMessage(null);

        ExportJobResponse response = ExportJobResponse.from(job);

        assertThat(response.errorMessage()).isNull();
    }
}
