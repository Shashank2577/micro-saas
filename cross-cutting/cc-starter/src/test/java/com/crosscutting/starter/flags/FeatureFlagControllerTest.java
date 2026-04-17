package com.crosscutting.starter.flags;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeatureFlagController.class)
@AutoConfigureMockMvc(addFilters = false)
class FeatureFlagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeatureFlagService featureFlagService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    @Test
    void evaluateAll_returnsAllFlagValues() throws Exception {
        Map<String, Boolean> flags = new LinkedHashMap<>();
        flags.put("dark-mode", true);
        flags.put("beta-feature", false);
        when(featureFlagService.evaluateAll(tenantId, userId)).thenReturn(flags);

        mockMvc.perform(get("/cc/flags")
                        .param("tenantId", tenantId.toString())
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dark-mode").value(true))
                .andExpect(jsonPath("$.beta-feature").value(false));
    }

    @Test
    void evaluateAll_worksWithoutParams() throws Exception {
        Map<String, Boolean> flags = Map.of("feature-x", true);
        when(featureFlagService.evaluateAll(null, null)).thenReturn(flags);

        mockMvc.perform(get("/cc/flags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feature-x").value(true));
    }

    @Test
    void evaluateFlag_returnsSingleFlagValue() throws Exception {
        when(featureFlagService.isEnabled("dark-mode", tenantId, userId)).thenReturn(true);

        mockMvc.perform(get("/cc/flags/dark-mode")
                        .param("tenantId", tenantId.toString())
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void evaluateFlag_returnsFalseWhenDisabled() throws Exception {
        when(featureFlagService.isEnabled("beta-feature", tenantId, userId)).thenReturn(false);

        mockMvc.perform(get("/cc/flags/beta-feature")
                        .param("tenantId", tenantId.toString())
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false));
    }

    @Test
    void createFlag_createsAndReturnsFlag() throws Exception {
        UUID createdFlagId = UUID.randomUUID();
        FeatureFlag created = new FeatureFlag();
        created.setId(createdFlagId);
        created.setKey("new-feature");
        created.setDescription("A new feature");
        created.setDefaultEnabled(true);
        when(featureFlagService.createFlag("new-feature", "A new feature", true))
                .thenReturn(created);

        mockMvc.perform(post("/cc/flags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"key":"new-feature","description":"A new feature","defaultEnabled":true}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("new-feature"))
                .andExpect(jsonPath("$.defaultEnabled").value(true))
                .andExpect(jsonPath("$.id").value(createdFlagId.toString()));
    }

    @Test
    void setOverride_setsAndReturnsOverride() throws Exception {
        UUID overrideId = UUID.randomUUID();
        UUID flagId = UUID.randomUUID();
        FlagOverride override = new FlagOverride();
        override.setId(overrideId);
        override.setFlagId(flagId);
        override.setTenantId(tenantId);
        override.setUserId(userId);
        override.setEnabled(true);
        when(featureFlagService.setOverride("dark-mode", tenantId, userId, true))
                .thenReturn(override);

        mockMvc.perform(put("/cc/flags/dark-mode/override")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tenantId":"%s","userId":"%s","enabled":true}
                                """.formatted(tenantId, userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.flagId").value(flagId.toString()));
    }
}
