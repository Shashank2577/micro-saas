package com.tenantmanager.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.tenantmanager.domain.CustomerTenant;
import com.tenantmanager.domain.OnboardingMilestone;
import com.tenantmanager.domain.TenantEvent;
import com.tenantmanager.dto.CreateEventRequest;
import com.tenantmanager.dto.CreateMilestoneRequest;
import com.tenantmanager.dto.CreateTenantRequest;
import com.tenantmanager.repository.CustomerTenantRepository;
import com.tenantmanager.repository.OnboardingMilestoneRepository;
import com.tenantmanager.repository.TenantEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock
    private CustomerTenantRepository tenantRepository;

    @Mock
    private OnboardingMilestoneRepository milestoneRepository;

    @Mock
    private TenantEventRepository eventRepository;

    @Mock
    private AiHealthService aiHealthService;

    @InjectMocks
    private TenantService tenantService;

    private UUID systemTenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(systemTenantId);
    }

    @Test
    void testCreateTenant() {
        CreateTenantRequest request = new CreateTenantRequest();
        request.setName("Acme Corp");

        when(tenantRepository.save(any(CustomerTenant.class))).thenAnswer(i -> {
            CustomerTenant t = i.getArgument(0);
            t.setId(UUID.randomUUID());
            return t;
        });

        CustomerTenant result = tenantService.createTenant(request);

        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
        assertEquals("ONBOARDING", result.getStatus());
    }

    @Test
    void testAnalyzeHealth() {
        UUID customerId = UUID.randomUUID();
        CustomerTenant tenant = new CustomerTenant();
        tenant.setId(customerId);
        tenant.setName("Acme");

        when(tenantRepository.findByIdAndTenantId(customerId, systemTenantId)).thenReturn(Optional.of(tenant));
        when(eventRepository.findByCustomerTenantIdAndTenantIdOrderByOccurredAtDesc(customerId, systemTenantId)).thenReturn(List.of());
        when(aiHealthService.analyzeHealth(any(), any())).thenReturn(new AiHealthService.HealthAnalysis(90, "LOW"));
        when(tenantRepository.save(any(CustomerTenant.class))).thenAnswer(i -> i.getArgument(0));

        CustomerTenant result = tenantService.analyzeHealth(customerId);

        assertEquals(90, result.getHealthScore());
        assertEquals("LOW", result.getChurnRisk());
    }
}
