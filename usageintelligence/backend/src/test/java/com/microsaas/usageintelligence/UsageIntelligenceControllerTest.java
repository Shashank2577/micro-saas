package com.microsaas.usageintelligence;

import com.crosscutting.starter.tenancy.TenantContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.usageintelligence.controller.UsageIntelligenceController;
import com.microsaas.usageintelligence.domain.Event;
import com.microsaas.usageintelligence.dto.CreateEventDto;
import com.microsaas.usageintelligence.service.UsageIntelligenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsageIntelligenceController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UsageIntelligenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsageIntelligenceService service;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        TenantContext.set(UUID.randomUUID());
    }

    @Test
    void testTrackEvent() throws Exception {
        CreateEventDto dto = new CreateEventDto();
        dto.setUserId("user1");
        dto.setEventName("login");
        dto.setProperties(Map.of("platform", "web"));

        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setTenantId(TenantContext.require());
        event.setUserId("user1");
        event.setEventName("login");
        event.setProperties(dto.getProperties());
        event.setCreatedAt(LocalDateTime.now());

        Mockito.when(service.trackEvent(any(CreateEventDto.class))).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.eventName").value("login"));
    }

    @Test
    void testGetEvents() throws Exception {
        Mockito.when(service.getRecentEvents()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk());
    }
}
