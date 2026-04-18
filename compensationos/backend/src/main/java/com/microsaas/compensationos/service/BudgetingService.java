package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.BudgetForecastResponse;
import com.microsaas.compensationos.dto.CycleScenarioRequest;
import com.microsaas.compensationos.dto.CycleScenarioResponse;
import com.microsaas.compensationos.entity.CompensationCycle;
import com.microsaas.compensationos.entity.EmployeeCompensation;
import com.microsaas.compensationos.repository.CompensationCycleRepository;
import com.microsaas.compensationos.repository.EmployeeCompensationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetingService {

    private final CompensationCycleRepository compensationCycleRepository;
    private final EmployeeCompensationRepository employeeCompensationRepository;

    public List<CompensationCycle> getCycles() {
        UUID tenantId = TenantContext.require();
        return compensationCycleRepository.findByTenantId(tenantId);
    }

    public CompensationCycle createCycle(CompensationCycle cycle) {
        UUID tenantId = TenantContext.require();
        cycle.setTenantId(tenantId);
        return compensationCycleRepository.save(cycle);
    }

    public CycleScenarioResponse modelScenario(UUID cycleId, CycleScenarioRequest request) {
        UUID tenantId = TenantContext.require();
        CompensationCycle cycle = compensationCycleRepository.findById(cycleId)
                .filter(c -> c.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Cycle not found"));

        List<EmployeeCompensation> employees = employeeCompensationRepository.findByTenantId(tenantId);
        BigDecimal currentTotalCost = employees.stream()
                .map(EmployeeCompensation::getBaseSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal multiplier = BigDecimal.ONE.add(request.getIncreasePercent().divide(new BigDecimal("100")));
        BigDecimal projectedCost = currentTotalCost.multiply(multiplier);

        CycleScenarioResponse response = new CycleScenarioResponse();
        response.setProjectedTotalCost(projectedCost);
        response.setRemainingBudget(request.getBudgetCap().subtract(projectedCost));
        response.setEligibleEmployees(employees.size());

        return response;
    }

    public BudgetForecastResponse forecastBudget(int months) {
        UUID tenantId = TenantContext.require();
        List<EmployeeCompensation> employees = employeeCompensationRepository.findByTenantId(tenantId);
        BigDecimal monthlyPayroll = employees.stream()
                .map(e -> e.getBaseSalary().divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> projections = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        BigDecimal annualForecast = BigDecimal.ZERO;

        for (int i = 0; i < months; i++) {
            String monthKey = now.plusMonths(i).getYear() + "-" + String.format("%02d", now.plusMonths(i).getMonthValue());
            projections.put(monthKey, monthlyPayroll);
            if (i < 12) {
                annualForecast = annualForecast.add(monthlyPayroll);
            }
        }

        BudgetForecastResponse response = new BudgetForecastResponse();
        response.setMonthlyProjections(projections);
        response.setTotalAnnualForecast(annualForecast);
        return response;
    }
}
