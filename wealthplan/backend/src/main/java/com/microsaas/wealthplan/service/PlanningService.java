package com.microsaas.wealthplan.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthplan.dto.RetirementReadinessDto;
import com.microsaas.wealthplan.dto.SavingsRateDto;
import com.microsaas.wealthplan.dto.ScenarioDto;
import com.microsaas.wealthplan.entity.Goal;
import com.microsaas.wealthplan.entity.Scenario;
import com.microsaas.wealthplan.repository.GoalRepository;
import com.microsaas.wealthplan.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanningService {
    private final ScenarioRepository scenarioRepository;
    private final GoalRepository goalRepository;

    public RetirementReadinessDto calculateRetirementReadiness(int age, BigDecimal desiredIncome, BigDecimal currentSavings, BigDecimal monthlyContribution) {
        // Simple calculation using 4% rule: Required Nest Egg = desiredIncome * 25
        BigDecimal requiredNestEgg = desiredIncome.multiply(new BigDecimal("25"));

        int yearsToRetirement = Math.max(0, 65 - age);
        int months = yearsToRetirement * 12;

        // Assumed 6% real return
        double r = 0.06 / 12;
        double futureValueSavings = currentSavings.doubleValue() * Math.pow(1 + r, months);

        double futureValueContributions = 0;
        if (r > 0) {
            futureValueContributions = monthlyContribution.doubleValue() * ((Math.pow(1 + r, months) - 1) / r);
        } else {
            futureValueContributions = monthlyContribution.doubleValue() * months;
        }

        BigDecimal projectedNestEgg = BigDecimal.valueOf(futureValueSavings + futureValueContributions)
                .setScale(2, RoundingMode.HALF_UP);

        double score = 0;
        if (requiredNestEgg.compareTo(BigDecimal.ZERO) > 0) {
            score = projectedNestEgg.divide(requiredNestEgg, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")).doubleValue();
        }

        return new RetirementReadinessDto(Math.min(100.0, score), projectedNestEgg, requiredNestEgg);
    }

    @Transactional
    public Scenario createScenario(ScenarioDto dto) {
        String tenantId = TenantContext.require().toString();

        Goal goal = goalRepository.findByIdAndTenantId(dto.getGoalId(), tenantId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        long scenarioCount = scenarioRepository.findByTenantIdAndGoalId(tenantId, goal.getId()).size();
        if (scenarioCount >= 20) {
            throw new RuntimeException("Maximum of 20 scenarios allowed per goal.");
        }

        Scenario scenario = new Scenario();
        scenario.setTenantId(tenantId);
        scenario.setGoalId(goal.getId());
        scenario.setName(dto.getName());
        scenario.setAssumedReturnRate(dto.getAssumedReturnRate());
        scenario.setInflationRate(dto.getInflationRate());
        scenario.setMonthlyContribution(dto.getMonthlyContribution());

        return scenarioRepository.save(scenario);
    }

    public SavingsRateDto calculateSavingsRate(UUID goalId) {
        Goal goal = goalRepository.findByIdAndTenantId(goalId, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        BigDecimal target = goal.getTargetAmount();
        BigDecimal current = goal.getCurrentAmount();
        BigDecimal shortfall = target.subtract(current);

        if (shortfall.compareTo(BigDecimal.ZERO) <= 0) {
            return new SavingsRateDto(BigDecimal.ZERO);
        }

        long months = ChronoUnit.MONTHS.between(LocalDate.now(), goal.getTargetDate());
        if (months <= 0) {
            return new SavingsRateDto(shortfall);
        }

        // Simple straight-line required savings (without compounding for a simple baseline)
        BigDecimal required = shortfall.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        return new SavingsRateDto(required);
    }
}
