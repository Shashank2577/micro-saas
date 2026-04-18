package com.microsaas.compensationos.service;

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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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
            when(aiService.generateInsight(anyString())).thenReturn("AI Analysis result");

            PayEquityAnalysisResponse response = payEquityService.analyzePayEquity("Engineer");

            assertNotNull(response);
            assertEquals(new BigDecimal("100000.00"), response.getAvgSalaryByGender().get("Male"));
            assertEquals(new BigDecimal("92500.00"), response.getAvgSalaryByGender().get("Female"));
            assertEquals("AI Analysis result", response.getAiInsight());
        }
    }
}
