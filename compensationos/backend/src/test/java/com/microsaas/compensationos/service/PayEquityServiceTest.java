package com.microsaas.compensationos.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.PayEquityAnalysisResponse;
import com.microsaas.compensationos.entity.EmployeeCompensation;
import com.microsaas.compensationos.repository.EmployeeCompensationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayEquityServiceTest {

    @Mock
    private EmployeeCompensationRepository employeeCompensationRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private PayEquityService payEquityService;

    @Test
    void analyzePayEquity_ReturnsAveragesAndInsight() {
        ReflectionTestUtils.setField(payEquityService, "defaultModel", "claude-sonnet-4-6");

        UUID tenantId = UUID.randomUUID();
        try (MockedStatic<TenantContext> mockedContext = mockStatic(TenantContext.class)) {
            mockedContext.when(TenantContext::require).thenReturn(tenantId);

            EmployeeCompensation emp1 = new EmployeeCompensation();
            emp1.setGender("Male");
            emp1.setBaseSalary(new BigDecimal("100000"));

            EmployeeCompensation emp2 = new EmployeeCompensation();
            emp2.setGender("Female");
            emp2.setBaseSalary(new BigDecimal("90000"));
            
            EmployeeCompensation emp3 = new EmployeeCompensation();
            emp3.setGender("Female");
            emp3.setBaseSalary(new BigDecimal("95000"));

            when(employeeCompensationRepository.findByTenantIdAndRole(tenantId, "Engineer")).thenReturn(List.of(emp1, emp2, emp3));

            // Note: we can mock any() to return a ChatResponse with correct content.
            // Since we don't know the exact class name for ChatResponse, let's mock it correctly.
            // But wait, the method signature is aiService.chat(ChatRequest).content().
            // I'll create a mock for the object returned by chat().
            ChatResponse mockResponse = mock(ChatResponse.class);
            when(mockResponse.content()).thenReturn("AI Analysis result");
            when(aiService.chat(any(ChatRequest.class))).thenReturn(mockResponse);

            PayEquityAnalysisResponse response = payEquityService.analyzePayEquity("Engineer");

            assertNotNull(response);
            assertEquals(new BigDecimal("100000.00"), response.getAvgSalaryByGender().get("Male"));
            assertEquals(new BigDecimal("92500.00"), response.getAvgSalaryByGender().get("Female"));
            assertEquals("AI Analysis result", response.getAiInsight());
        }
    }
}
