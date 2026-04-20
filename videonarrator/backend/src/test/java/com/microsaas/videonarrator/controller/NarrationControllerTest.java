package com.microsaas.videonarrator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.videonarrator.domain.NarrationTrack;
import com.microsaas.videonarrator.dto.NarrateRequest;
import com.microsaas.videonarrator.dto.VoiceDto;
import com.microsaas.videonarrator.service.NarrationService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(controllers = NarrationController.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration"
    })
@AutoConfigureMockMvc(addFilters = false)
class NarrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NarrationService narrationService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void getVoices_ReturnsVoices() throws Exception {
        when(narrationService.getVoices()).thenReturn(List.of(new VoiceDto("alloy", "OPENAI", "Alloy")));

        mockMvc.perform(get("/api/v1/voices")
                        .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("alloy"));
    }

    @Test
    void narrate_ReturnsNarrationTrack() throws Exception {
        UUID projectId = UUID.randomUUID();
        NarrateRequest request = new NarrateRequest();
        request.setVoiceProvider("OPENAI");
        request.setVoiceId("alloy");

        NarrationTrack track = new NarrationTrack();
        track.setId(UUID.randomUUID());
        track.setStatus(NarrationTrack.NarrationStatus.PENDING);

        when(narrationService.startNarration(eq(projectId), eq(tenantId.toString()), any(NarrateRequest.class)))
                .thenReturn(track);

        mockMvc.perform(post("/api/v1/projects/" + projectId + "/narrate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
