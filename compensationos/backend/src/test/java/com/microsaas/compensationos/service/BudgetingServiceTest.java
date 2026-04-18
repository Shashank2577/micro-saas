package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.BudgetForecastResponse;
import com.microsaas.compensationos.dto.CycleScenarioRequest;
import com.microsaas.compensationos.dto.CycleScenarioResponse;
import com.microsaas.compensationos.entity.CompensationCycle;
import com.microsaas.compensationos.entity.EmployeeCompensation;
import com.microsaas.compensationos.repository.CompensationCycleRepository;
import com.microsaas.compensationos.repository.EmployeeCompensationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetingServiceTest {

    @Mock
    private CompensationCycleRepository compensationCycleRepository;
    
    @Mock
    private EmployeeCompensationRepository employeeCompensationRepository;

    @InjectMocks
    private BudgetingService budgetingService;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void modelScenario_CalculatesCorrectly() {
        try (MockedStatic<TenantContext> mockedContext = mockStatic(TenantContext.class)) {
            mockedContext.when(TenantContext::require).thenReturn(tenantId);

            UUID cycleId = UUID.randomUUID();
            CompensationCycle cycle = new CompensationCycle();
            cycle.setId(cycleId);
            cycle.setTenantId(tenantId);

            EmployeeCompensation emp1 = new EmployeeCompensation();
            emp1.setBaseSalary(new BigDecimal("100000"));
            EmployeeCompensation emp2 = new EmployeeCompensation();
            emp2.setBaseSalary(new BigDecimal("150000"));

            when(compensationCycleRepository.findById(cycleId)).thenReturn(Optional.of(cycle));
            when(employeeCompensationRepository.findByTenantId(tenantId)).thenReturn(List.of(emp1, emp2));

            CycleScenarioRequest request = new CycleScenarioRequest();
            request.setIncreasePercent(new BigDecimal("5"));
            request.setBudgetCap(new BigDecimal("300000"));

            CycleScenarioResponse response = budgetingService.modelScenario(cycleId, request);

            assertNotNull(response);
            assertEquals(new BigDecimal("262500.00"), response.getProjectedTotalCost()); // 250k * 1.05
            assertEquals(new BigDecimal("37500.00"), response.getRemainingBudget());
            assertEquals(2, response.getEligibleEmployees());
        }
    }
}
