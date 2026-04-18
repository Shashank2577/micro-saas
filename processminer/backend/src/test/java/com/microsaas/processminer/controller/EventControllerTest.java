package com.microsaas.processminer.controller;

import com.microsaas.processminer.domain.EventLog;
import com.microsaas.processminer.service.IngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = EventController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngestionService ingestionService;

    @Test
    void ingestEvent_Success() throws Exception {
        EventLog mockEvent = new EventLog();
        mockEvent.setId(UUID.randomUUID());
        mockEvent.setSystemType("ERP");
        mockEvent.setCaseId("C-123");
        mockEvent.setActivityName("Order Received");
        mockEvent.setTimestamp(LocalDateTime.now());

        when(ingestionService.ingestEvent(any())).thenReturn(mockEvent);

        String json = """
            {
                "systemType": "ERP",
                "caseId": "C-123",
                "activityName": "Order Received",
                "timestamp": "2023-10-01T10:00:00"
            }
            """;

        mockMvc.perform(post("/api/events/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.systemType").value("ERP"));
    }
}
