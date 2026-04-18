package com.microsaas.taskqueue.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.taskqueue.domain.Job;
import com.microsaas.taskqueue.domain.JobStatus;
import com.microsaas.taskqueue.dto.JobRequest;
import com.microsaas.taskqueue.service.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JobController.class, properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"})
@ActiveProfiles("test")
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testEnqueueJob() throws Exception {
        JobRequest req = new JobRequest();
        req.setName("send_email");
        
        Job job = new Job();
        job.setId(UUID.randomUUID());
        job.setName("send_email");
        job.setStatus(JobStatus.PENDING);

        when(jobService.enqueueJob(any(JobRequest.class))).thenReturn(job);

        mockMvc.perform(post("/api/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("send_email"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
