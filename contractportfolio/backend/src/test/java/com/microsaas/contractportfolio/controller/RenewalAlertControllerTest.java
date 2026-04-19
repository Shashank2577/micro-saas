package com.microsaas.contractportfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.contractportfolio.domain.RenewalAlert;
import com.microsaas.contractportfolio.service.RenewalAlertService;
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
class RenewalAlertControllerTest {



    @org.springframework.boot.test.mock.mockito.MockBean
    private com.crosscutting.starter.ai.AiService aiService;









    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RenewalAlertService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {
        RenewalAlert entity = new RenewalAlert();
        entity.setName("Test Alert");
        entity.setStatus("ACTIVE");

        when(service.create(any(RenewalAlert.class))).thenReturn(entity);

        mockMvc.perform(post("/api/v1/contracts/renewal-alerts")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        UUID id = UUID.randomUUID();
        RenewalAlert entity = new RenewalAlert();
        entity.setId(id);

        when(service.getById(eq(id), any(UUID.class))).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/api/v1/contracts/renewal-alerts/{id}", id)
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }
}
