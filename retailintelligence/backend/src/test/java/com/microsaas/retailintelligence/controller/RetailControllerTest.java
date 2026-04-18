package com.microsaas.retailintelligence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.retailintelligence.dto.CreateSkuRequest;
import com.microsaas.retailintelligence.dto.SkuDto;
import com.microsaas.retailintelligence.service.RetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RetailController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ActiveProfiles("test")
public class RetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetailService retailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateSku() throws Exception {
        CreateSkuRequest req = new CreateSkuRequest();
        req.setSkuCode("CODE1");
        
        SkuDto dto = new SkuDto();
        dto.setSkuCode("CODE1");
        dto.setId(UUID.randomUUID());
        
        when(retailService.createSku(any())).thenReturn(dto);

        mockMvc.perform(post("/api/skus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skuCode").value("CODE1"));
    }
}
