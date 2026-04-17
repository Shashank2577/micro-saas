package com.microsaas.goaltracker.service;

import com.microsaas.goaltracker.entity.Goal;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class SavingsCalculationService {

    // Calculate required monthly savings with conservative interest
    public BigDecimal calculateMonthlySavings(Goal goal) {
        BigDecimal remainingAmount = goal.getTargetAmount().subtract(goal.getCurrentAmount());
        if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        long monthsRemaining = ChronoUnit.MONTHS.between(LocalDateTime.now(), goal.getDeadline());
        if (monthsRemaining <= 0) {
            return remainingAmount;
        }

        // Simplistic conservative assumption (e.g. 1% APY -> ~0.00083 monthly interest)
        // For simple calculation right now, we do linear savings.
        return remainingAmount.divide(new BigDecimal(monthsRemaining), 2, RoundingMode.HALF_UP);
    }
}
