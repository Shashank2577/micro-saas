package com.microsaas.ghostwriter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.ghostwriter.domain.Document;
import com.microsaas.ghostwriter.dto.GenerateRequest;
import com.microsaas.ghostwriter.service.DocumentService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DocumentController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void getAllDocuments_ReturnsOk() throws Exception {
        Document doc = new Document();
        doc.setTitle("Test Doc");
        when(documentService.getAllDocuments(eq(tenantId), any())).thenReturn(List.of(doc));

        mockMvc.perform(get("/api/documents")
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Doc"));
    }

    @Test
    void generateDocument_ReturnsCreated() throws Exception {
        Document doc = new Document();
        doc.setTitle("New Doc");
        doc.setStatus("COMPLETED");

        when(documentService.generateDocument(eq(tenantId), any())).thenReturn(doc);

        GenerateRequest request = new GenerateRequest();
        request.setTopic("New Doc");

        mockMvc.perform(post("/api/documents/generate")
                .header("X-Tenant-ID", tenantId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Doc"));
    }
}
