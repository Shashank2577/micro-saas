package com.microsaas.onboardflow.controller;
import com.microsaas.onboardflow.model.OnboardingPlan;
import com.microsaas.onboardflow.service.OnboardingPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
class OnboardingPlanControllerTest {
    @Mock private OnboardingPlanService service;
    @InjectMocks private OnboardingPlanController controller;
    private final UUID tenantId = UUID.randomUUID();
    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }
    @Test void findAll_ReturnsList() {
        when(service.findAll(tenantId)).thenReturn(Collections.emptyList());
        ResponseEntity<List<OnboardingPlan>> response = controller.findAll(tenantId);
        assertEquals(200, response.getStatusCode().value());
        verify(service, times(1)).findAll(tenantId);
    }
}
