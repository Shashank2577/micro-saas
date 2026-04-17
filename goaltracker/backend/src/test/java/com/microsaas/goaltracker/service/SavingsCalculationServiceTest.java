package com.microsaas.goaltracker.service;

import com.microsaas.goaltracker.entity.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SavingsCalculationServiceTest {

    private SavingsCalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new SavingsCalculationService();
    }

    @Test
    void testCalculateMonthlySavings() {
        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("12000.00"));
        goal.setCurrentAmount(new BigDecimal("2000.00"));
        // Need 10 full months ahead to result in exactly 10 months distance.
        goal.setDeadline(LocalDateTime.now().plusMonths(10).plusDays(1));

        BigDecimal monthly = calculationService.calculateMonthlySavings(goal);

        // 10000 remaining / 10 months = 1000.00
        assertEquals(new BigDecimal("1000.00"), monthly);
    }
}
