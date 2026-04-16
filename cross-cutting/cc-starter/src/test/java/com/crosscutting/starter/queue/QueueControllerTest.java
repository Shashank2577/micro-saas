package com.crosscutting.starter.queue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QueueController.class)
@AutoConfigureMockMvc(addFilters = false)
class QueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueueService queueService;

    @Test
    void getStatsReturnsQueueDepths() throws Exception {
        List<Map<String, Object>> stats = List.of(
                Map.of("queue_name", "cc_jobs", "status", "pending", "count", 5)
        );
        when(queueService.getQueueStats()).thenReturn(stats);

        mockMvc.perform(get("/cc/queues/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].queue_name").value("cc_jobs"))
                .andExpect(jsonPath("$[0].status").value("pending"))
                .andExpect(jsonPath("$[0].count").value(5));
    }

    @Test
    void getDeadLetterJobsReturnsList() throws Exception {
        Job job = new Job(1L, "cc_jobs", "{\"type\":\"email\"}", 5, 5,
                Instant.parse("2025-01-01T00:00:00Z"), Instant.parse("2025-01-01T00:00:00Z"));
        when(queueService.getDeadLetterJobs("cc_jobs")).thenReturn(List.of(job));

        mockMvc.perform(get("/cc/queues/cc_jobs/dead-letter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].queueName").value("cc_jobs"))
                .andExpect(jsonPath("$[0].attemptCount").value(5));
    }

    @Test
    void getDeadLetterJobsReturnsEmptyList() throws Exception {
        when(queueService.getDeadLetterJobs("cc_exports")).thenReturn(List.of());

        mockMvc.perform(get("/cc/queues/cc_exports/dead-letter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void retryDeadLetterJobCallsService() throws Exception {
        mockMvc.perform(post("/cc/queues/cc_jobs/dead-letter/42/retry"))
                .andExpect(status().isOk());

        verify(queueService).retryDeadLetterJob(42L);
    }
}
