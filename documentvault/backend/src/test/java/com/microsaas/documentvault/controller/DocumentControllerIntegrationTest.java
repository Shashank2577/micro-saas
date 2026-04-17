package com.microsaas.documentvault.controller;

import com.microsaas.documentvault.model.Document;
import com.microsaas.documentvault.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "cc.auth.realm=test",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.flyway.enabled=false"
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class DocumentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;
    
    // Mocking to avoid actual instantiation
    @MockBean
    private com.microsaas.documentvault.service.AuditService auditService;
    
    @MockBean
    private com.microsaas.documentvault.repository.AuditLogRepository auditLogRepository;

    @Test
    public void testUploadDocument() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes());
        Document mockDoc = new Document();
        mockDoc.setId(UUID.randomUUID());

        when(documentService.uploadDocument(any(), any(), any(), any())).thenReturn(mockDoc);

        mockMvc.perform(multipart("/api/documents/upload")
                .file(file)
                .param("title", "Test Title")
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }
}
