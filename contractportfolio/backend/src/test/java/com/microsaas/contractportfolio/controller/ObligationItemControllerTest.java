package com.microsaas.contractportfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.contractportfolio.domain.ObligationItem;
import com.microsaas.contractportfolio.service.ObligationItemService;
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
class ObligationItemControllerTest {



    @org.springframework.boot.test.mock.mockito.MockBean
    private com.crosscutting.starter.ai.AiService aiService;









    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObligationItemService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {
        ObligationItem entity = new ObligationItem();
        entity.setName("Test Obligation");
        entity.setStatus("ACTIVE");

        when(service.create(any(ObligationItem.class))).thenReturn(entity);

        mockMvc.perform(post("/api/v1/contracts/obligation-items")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        UUID id = UUID.randomUUID();
        ObligationItem entity = new ObligationItem();
        entity.setId(id);

        when(service.getById(eq(id), any(UUID.class))).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/api/v1/contracts/obligation-items/{id}", id)
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }
}
