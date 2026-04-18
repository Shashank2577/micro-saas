package com.microsaas.datagovernanceos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.datagovernanceos.entity.DataAsset;
import com.microsaas.datagovernanceos.service.DataClassificationAiService;
import com.microsaas.datagovernanceos.service.GovernanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@WebMvcTest(controllers = GovernanceController.class, 
            excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class GovernanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GovernanceService governanceService;

    @MockBean
    private DataClassificationAiService aiService;

    @Test
    void testListAssets() throws Exception {
        when(governanceService.listAssets()).thenReturn(List.of(new DataAsset()));

        mockMvc.perform(get("/api/v1/assets")
                .header("X-Tenant-ID", "00000000-0000-0000-0000-000000000001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
