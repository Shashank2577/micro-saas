package com.microsaas.documentintelligence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.documentintelligence.dto.DocumentDTO;
import com.microsaas.documentintelligence.dto.QAQuery;
import com.microsaas.documentintelligence.dto.SearchQuery;
import com.microsaas.documentintelligence.service.DocumentService;
import com.microsaas.documentintelligence.service.QAService;
import com.microsaas.documentintelligence.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private QAService qaService;

    @MockBean
    private SearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testUploadDocument() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
        DocumentDTO dto = new DocumentDTO();
        dto.setId(UUID.randomUUID());
        Mockito.when(documentService.uploadDocument(any())).thenReturn(dto);

        mockMvc.perform(multipart("/api/documents/upload").file(file)
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDocument() throws Exception {
        Mockito.when(documentService.getDocument(any())).thenReturn(new DocumentDTO());

        mockMvc.perform(get("/api/documents/" + UUID.randomUUID())
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearch() throws Exception {
        SearchQuery query = new SearchQuery();
        query.setQuery("test query");

        mockMvc.perform(post("/api/documents/search")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAskQuestion() throws Exception {
        QAQuery query = new QAQuery();
        query.setQuestion("what is this?");

        mockMvc.perform(post("/api/documents/" + UUID.randomUUID() + "/qa")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk());
    }
}
