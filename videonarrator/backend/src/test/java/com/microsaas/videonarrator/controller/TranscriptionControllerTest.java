package com.microsaas.videonarrator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.videonarrator.domain.Transcription;
import com.microsaas.videonarrator.dto.TranscribeRequest;
import com.microsaas.videonarrator.service.TranscriptionService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TranscriptionController.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration"
    })
@AutoConfigureMockMvc(addFilters = false)
class TranscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranscriptionService transcriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void transcribe_ReturnsTranscription() throws Exception {
        UUID projectId = UUID.randomUUID();
        TranscribeRequest request = new TranscribeRequest();
        request.setLanguageCode("en");

        Transcription transcription = new Transcription();
        transcription.setId(UUID.randomUUID());
        transcription.setLanguageCode("en");
        transcription.setStatus(Transcription.TranscriptionStatus.PENDING);

        when(transcriptionService.startTranscription(eq(projectId), eq(tenantId.toString()), eq("en")))
                .thenReturn(transcription);

        mockMvc.perform(post("/api/v1/projects/" + projectId + "/transcribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
