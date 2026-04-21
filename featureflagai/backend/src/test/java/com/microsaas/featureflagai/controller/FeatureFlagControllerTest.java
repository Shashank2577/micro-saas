package com.microsaas.featureflagai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.featureflagai.domain.FeatureFlag;
import com.microsaas.featureflagai.repository.FeatureFlagRepository;
import com.microsaas.featureflagai.service.FlagCleanupService;
import com.microsaas.featureflagai.service.FlagEvaluationService;
import com.microsaas.featureflagai.service.ImpactAnalysisService;
import com.microsaas.featureflagai.service.RolloutService;
import com.microsaas.featureflagai.service.SegmentTargetingService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FeatureFlagController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    com.crosscutting.starter.ai.AiAutoConfiguration.class,
    com.crosscutting.starter.queue.QueueAutoConfiguration.class
})
class FeatureFlagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FeatureFlagRepository featureFlagRepository;

    @MockBean
    private FlagEvaluationService evaluationService;

    @MockBean
    private RolloutService rolloutService;

    @MockBean
    private ImpactAnalysisService impactAnalysisService;

    @MockBean
    private FlagCleanupService flagCleanupService;

    @MockBean
    private SegmentTargetingService segmentTargetingService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateAndListFlags() throws Exception {
        FeatureFlag flag = new FeatureFlag();
        flag.setName("New Feature");
        flag.setEnabled(true);
        flag.setRolloutPct(50);

        when(featureFlagRepository.save(any())).thenReturn(flag);
        when(featureFlagRepository.findByTenantId(tenantId)).thenReturn(List.of(flag));

        mockMvc.perform(post("/api/flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Feature"));

        mockMvc.perform(get("/api/flags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("New Feature"));
    }

    @Test
    void testEvaluateFlag() throws Exception {
        FeatureFlag flag = new FeatureFlag();
        flag.setId(UUID.randomUUID());
        flag.setTenantId(tenantId);
        flag.setName("Test Flag");
        flag.setEnabled(true);
        flag.setRolloutPct(100);

        when(evaluationService.evaluate(eq(flag.getId()), eq("user123"), any())).thenReturn(true);

        FeatureFlagController.EvaluationRequest request = new FeatureFlagController.EvaluationRequest();
        request.setUserId("user123");

        mockMvc.perform(post("/api/flags/" + flag.getId() + "/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
