package com.crosscutting.starter.export;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExportService exportService;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void startExport_returnsCreatedWithJobId() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(exportService.startExport(eq(tenantId), eq("users"), eq("csv"), eq("active=true"), eq("name,email")))
                .thenReturn(jobId);

        mockMvc.perform(post("/cc/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tenantId":"%s","resourceType":"users","format":"csv","query":"active=true","columns":"name,email"}
                                """.formatted(tenantId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobId").value(jobId.toString()));
    }

    @Test
    void getJobStatus_returnsJob() throws Exception {
        UUID jobId = UUID.randomUUID();
        ExportJob job = new ExportJob();
        job.setId(jobId);
        job.setTenantId(tenantId);
        job.setResourceType("users");
        job.setStatus("completed");
        job.setFormat("csv");
        job.setResultUrl("https://storage.example.com/export/file.csv");

        when(exportService.getJobStatus(jobId)).thenReturn(job);

        mockMvc.perform(get("/cc/export/{jobId}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jobId.toString()))
                .andExpect(jsonPath("$.status").value("completed"))
                .andExpect(jsonPath("$.resultUrl").value("https://storage.example.com/export/file.csv"));
    }

    @Test
    void startImport_returnsCreatedWithJobId() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(exportService.startImport(eq(tenantId), eq("users"), eq("uploads/file.csv")))
                .thenReturn(jobId);

        mockMvc.perform(post("/cc/export/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tenantId":"%s","resourceType":"users","fileKey":"uploads/file.csv"}
                                """.formatted(tenantId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobId").value(jobId.toString()));
    }

    @Test
    void listJobs_returnsPagedJobs() throws Exception {
        ExportJob job = new ExportJob();
        job.setId(UUID.randomUUID());
        job.setTenantId(tenantId);
        job.setResourceType("users");
        job.setStatus("pending");
        job.setFormat("csv");

        when(exportService.listJobs(eq(tenantId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(job), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/cc/export/jobs")
                        .param("tenantId", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].resourceType").value("users"))
                .andExpect(jsonPath("$.content[0].status").value("pending"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
