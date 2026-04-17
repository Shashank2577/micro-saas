package com.microsaas.wealthplan.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthplan.entity.Goal;
import com.microsaas.wealthplan.entity.MonteCarloResult;
import com.microsaas.wealthplan.entity.Scenario;
import com.microsaas.wealthplan.repository.GoalRepository;
import com.microsaas.wealthplan.repository.MonteCarloResultRepository;
import com.microsaas.wealthplan.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonteCarloService {
    private final ScenarioRepository scenarioRepository;
    private final GoalRepository goalRepository;
    private final MonteCarloResultRepository resultRepository;

    @Transactional
    @Async
    public CompletableFuture<MonteCarloResult> runSimulationAsync(UUID scenarioId, String tenantId) {
        Scenario scenario = scenarioRepository.findByIdAndTenantId(scenarioId, tenantId)
                .orElseThrow(() -> new RuntimeException("Scenario not found"));

        Goal goal = goalRepository.findByIdAndTenantId(scenario.getGoalId(), tenantId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        long months = ChronoUnit.MONTHS.between(LocalDate.now(), goal.getTargetDate());
        if (months <= 0) {
            throw new RuntimeException("Target date must be in the future");
        }

        int simulations = 1000;
        double meanReturn = scenario.getAssumedReturnRate().doubleValue() / 12;
        double volatility = 0.15 / Math.sqrt(12); // Assuming 15% annual volatility

        NormalDistribution dist = new NormalDistribution(meanReturn, volatility);
        DescriptiveStatistics stats = new DescriptiveStatistics();

        int successes = 0;
        double target = goal.getTargetAmount().doubleValue();

        for (int i = 0; i < simulations; i++) {
            double balance = goal.getCurrentAmount().doubleValue();
            for (int m = 0; m < months; m++) {
                double monthlyReturn = dist.sample();
                balance = balance * (1 + monthlyReturn) + scenario.getMonthlyContribution().doubleValue();
            }
            stats.addValue(balance);
            if (balance >= target) {
                successes++;
            }
        }

        MonteCarloResult result = new MonteCarloResult();
        result.setTenantId(tenantId);
        result.setScenarioId(scenarioId);
        result.setSuccessProbability(BigDecimal.valueOf((double) successes / simulations).setScale(4, RoundingMode.HALF_UP));
        result.setMedianEndingBalance(BigDecimal.valueOf(stats.getPercentile(50)).setScale(2, RoundingMode.HALF_UP));
        result.setWorstCaseBalance(BigDecimal.valueOf(stats.getPercentile(5)).setScale(2, RoundingMode.HALF_UP));
        result.setBestCaseBalance(BigDecimal.valueOf(stats.getPercentile(95)).setScale(2, RoundingMode.HALF_UP));

        return CompletableFuture.completedFuture(resultRepository.save(result));
    }

    @Transactional
    public MonteCarloResult runSimulation(UUID scenarioId) {
        String tenantId = TenantContext.require().toString();
        try {
            return runSimulationAsync(scenarioId, tenantId).get();
        } catch (Exception e) {
            throw new RuntimeException("Error running Monte Carlo simulation", e);
        }
    }
}
