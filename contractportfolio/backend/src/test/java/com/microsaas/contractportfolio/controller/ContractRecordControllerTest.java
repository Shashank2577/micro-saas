package com.microsaas.contractportfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.contractportfolio.domain.ContractRecord;
import com.microsaas.contractportfolio.service.ContractRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContractRecordControllerTest {



    @org.springframework.boot.test.mock.mockito.MockBean
    private com.crosscutting.starter.ai.AiService aiService;









    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractRecordService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {
        ContractRecord entity = new ContractRecord();
        entity.setName("Test Record");
        entity.setStatus("ACTIVE");

        when(service.create(any(ContractRecord.class))).thenReturn(entity);

        mockMvc.perform(post("/api/v1/contracts/contract-records")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        UUID id = UUID.randomUUID();
        ContractRecord entity = new ContractRecord();
        entity.setId(id);

        when(service.getById(eq(id), any(UUID.class))).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/api/v1/contracts/contract-records/{id}", id)
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }
}
