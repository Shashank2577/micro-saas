package com.micro.interviewos.controller;

import com.micro.interviewos.dto.EvaluationRecordDTO;
import com.micro.interviewos.service.EvaluationRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EvaluationRecordController.class, excludeAutoConfiguration = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
class EvaluationRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluationRecordService service;

    private UUID id;
    private UUID tenantId;
    private EvaluationRecordDTO dto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        tenantId = UUID.randomUUID();
        dto = new EvaluationRecordDTO();
        dto.setId(id);
        dto.setTenantId(tenantId);
        dto.setName("Test Name");
    }

    @Test
    void findAll_ShouldReturnList() throws Exception {
        when(service.findAll(tenantId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/interviews/evaluation-records")
                .header("X-Tenant-Id", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Name"));
    }

    @Test
    void findById_ShouldReturnDto() throws Exception {
        when(service.findById(id, tenantId)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/v1/interviews/evaluation-records/{id}", id)
                .header("X-Tenant-Id", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Name"));
    }
}
