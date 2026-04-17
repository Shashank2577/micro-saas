package com.microsaas.wealthplan.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthplan.dto.GoalDto;
import com.microsaas.wealthplan.dto.ProgressDto;
import com.microsaas.wealthplan.entity.Goal;
import com.microsaas.wealthplan.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;

    @Transactional
    public Goal createGoal(GoalDto dto) {
        String tenantId = TenantContext.require().toString();
        long count = goalRepository.findByTenantId(tenantId).size();
        if (count >= 50) {
            throw new RuntimeException("Maximum of 50 goals allowed per user.");
        }
        Goal goal = new Goal();
        goal.setTenantId(tenantId);
        goal.setName(dto.getName());
        goal.setType(dto.getType());
        goal.setTargetAmount(dto.getTargetAmount());
        goal.setCurrentAmount(dto.getCurrentAmount());
        goal.setTargetDate(dto.getTargetDate());
        return goalRepository.save(goal);
    }

    public List<Goal> getGoals() {
        return goalRepository.findByTenantId(TenantContext.require().toString());
    }

    public Goal getGoal(UUID id) {
        return goalRepository.findByIdAndTenantId(id, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }

    @Transactional
    public Goal updateGoal(UUID id, GoalDto dto) {
        Goal goal = getGoal(id);
        goal.setName(dto.getName());
        goal.setType(dto.getType());
        goal.setTargetAmount(dto.getTargetAmount());
        goal.setCurrentAmount(dto.getCurrentAmount());
        goal.setTargetDate(dto.getTargetDate());
        return goalRepository.save(goal);
    }

    @Transactional
    public void deleteGoal(UUID id) {
        Goal goal = getGoal(id);
        goalRepository.delete(goal);
    }

    public ProgressDto getProgress(UUID id) {
        Goal goal = getGoal(id);
        if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
            return new ProgressDto(100.0);
        }
        double percentage = goal.getCurrentAmount()
                .divide(goal.getTargetAmount(), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .doubleValue();
        return new ProgressDto(Math.min(100.0, percentage));
    }
}
