package com.microsaas.retirementplus.controller;

import com.microsaas.retirementplus.domain.UserProfile;
import com.microsaas.retirementplus.dto.ProfileDto;
import com.microsaas.retirementplus.service.ProfileService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID tenantId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void createOrUpdateProfile() throws Exception {
        ProfileDto dto = new ProfileDto();
        dto.setUserId(userId);
        dto.setCurrentAge(60);

        UserProfile profile = new UserProfile();
        profile.setId(UUID.randomUUID());
        profile.setUserId(userId);
        profile.setCurrentAge(60);

        try (MockedStatic<TenantContext> mockedTenantContext = Mockito.mockStatic(TenantContext.class)) {
            mockedTenantContext.when(TenantContext::require).thenReturn(tenantId);
            when(profileService.createOrUpdateProfile(any(ProfileDto.class), any(UUID.class))).thenReturn(profile);

            mockMvc.perform(post("/api/profiles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.currentAge").value(60));
        }
    }
}
