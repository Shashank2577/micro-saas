package com.microsaas.wealthedge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.wealthedge.domain.Asset;
import com.microsaas.wealthedge.domain.AssetType;
import com.microsaas.wealthedge.service.AssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AssetController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
    }
)
@ActiveProfiles("test")
public class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetService assetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllAssets() throws Exception {
        Asset asset = new Asset();
        asset.setId(UUID.randomUUID());
        asset.setName("Real Estate");
        
        when(assetService.getAllAssets()).thenReturn(List.of(asset));

        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Real Estate"));
    }

    @Test
    void testCreateAsset() throws Exception {
        Asset input = new Asset();
        input.setName("New House");
        input.setType(AssetType.REAL_ESTATE);
        input.setCurrentValue(new BigDecimal("1000000"));

        Asset output = new Asset();
        output.setId(UUID.randomUUID());
        output.setName("New House");

        when(assetService.createAsset(any(Asset.class))).thenReturn(output);

        mockMvc.perform(post("/api/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New House"));
    }
}
