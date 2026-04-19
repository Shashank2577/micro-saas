package com.microsaas.copyoptimizer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.copyoptimizer.model.CopyAsset;
import com.microsaas.copyoptimizer.service.CopyAssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CopyAssetController.class)
@org.springframework.boot.test.mock.mockito.MockBean(com.microsaas.copyoptimizer.service.CopyAiService.class)
class CopyAssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CopyAssetService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_ShouldReturnOk() throws Exception {
        UUID tenantId = UUID.randomUUID();
        CopyAsset asset = new CopyAsset();
        asset.setName("Test Asset");

        CopyAsset created = new CopyAsset();
        created.setId(UUID.randomUUID());
        created.setName("Test Asset");

        when(service.create(any(CopyAsset.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/copy/copy-assets")
                .header("X-Tenant-ID", tenantId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(asset)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Asset"));
    }

    @Test
    void get_ShouldReturnOk() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        CopyAsset asset = new CopyAsset();
        asset.setId(id);
        asset.setName("Test Asset");

        when(service.getById(id, tenantId)).thenReturn(asset);

        mockMvc.perform(get("/api/v1/copy/copy-assets/{id}", id)
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Asset"));
    }
}
