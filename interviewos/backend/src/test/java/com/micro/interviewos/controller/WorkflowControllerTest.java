package com.micro.interviewos.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WorkflowController.class, excludeAutoConfiguration = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void execute_ShouldReturnStatus() throws Exception {
        mockMvc.perform(post("/api/v1/interviews/workflows/execute")
                .header("X-Tenant-Id", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("executed"));
    }
}
