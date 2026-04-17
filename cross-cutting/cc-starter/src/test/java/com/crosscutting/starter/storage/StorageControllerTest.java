package com.crosscutting.starter.storage;

import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StorageController.class)
@AutoConfigureMockMvc(addFilters = false)
class StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;

    @MockBean
    private MinioClient minioClient;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void getUploadUrl_returnsPresignedUrl() throws Exception {
        when(storageService.getUploadUrl(tenantId, "documents", "report.pdf",
                "application/pdf", 3600))
                .thenReturn("https://minio.example.com/presigned-put");

        mockMvc.perform(post("/cc/storage/upload-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tenantId":"%s","bucket":"documents","fileName":"report.pdf","contentType":"application/pdf","expiresInSeconds":3600}
                                """.formatted(tenantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://minio.example.com/presigned-put"));
    }

    @Test
    void getDownloadUrl_returnsPresignedUrl() throws Exception {
        when(storageService.getDownloadUrl(tenantId, "documents", "report.pdf", 3600))
                .thenReturn("https://minio.example.com/presigned-get");

        mockMvc.perform(post("/cc/storage/download-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tenantId":"%s","bucket":"documents","key":"report.pdf","expiresInSeconds":3600}
                                """.formatted(tenantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://minio.example.com/presigned-get"));
    }

    @Test
    void getUploadUrl_usesDefaultExpiry() throws Exception {
        when(storageService.getUploadUrl(tenantId, "docs", "f.txt", "text/plain", 3600))
                .thenReturn("https://minio.example.com/url");

        mockMvc.perform(post("/cc/storage/upload-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tenantId":"%s","bucket":"docs","fileName":"f.txt","contentType":"text/plain","expiresInSeconds":0}
                                """.formatted(tenantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://minio.example.com/url"));
    }

    @Test
    void deleteFile_returnsDeletedStatus() throws Exception {
        mockMvc.perform(delete("/cc/storage/files")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tenantId":"%s","bucket":"documents","key":"report.pdf"}
                                """.formatted(tenantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("deleted"));

        verify(storageService).deleteFile(tenantId, "documents", "report.pdf");
    }

    @Test
    void listFiles_returnsFileList() throws Exception {
        StorageService.FileInfo file1 = new StorageService.FileInfo(
                tenantId + "/documents/report.pdf", 1024, false, "2026-01-01T00:00:00Z");
        StorageService.FileInfo file2 = new StorageService.FileInfo(
                tenantId + "/documents/image.png", 2048, false, "2026-01-02T00:00:00Z");
        when(storageService.listFiles(tenantId, "documents", ""))
                .thenReturn(List.of(file1, file2));

        mockMvc.perform(get("/cc/storage/files")
                        .param("tenantId", tenantId.toString())
                        .param("bucket", "documents")
                        .param("prefix", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].key").value(tenantId + "/documents/report.pdf"))
                .andExpect(jsonPath("$[0].size").value(1024))
                .andExpect(jsonPath("$[1].key").value(tenantId + "/documents/image.png"));
    }

    @Test
    void listFiles_withoutPrefix_returnsAllFiles() throws Exception {
        when(storageService.listFiles(eq(tenantId), eq("documents"), eq(null)))
                .thenReturn(List.of());

        mockMvc.perform(get("/cc/storage/files")
                        .param("tenantId", tenantId.toString())
                        .param("bucket", "documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
