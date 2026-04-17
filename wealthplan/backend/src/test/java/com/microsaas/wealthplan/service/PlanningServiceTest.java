package com.microsaas.wealthplan.service;

import com.microsaas.wealthplan.dto.RetirementReadinessDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlanningServiceTest {

    @InjectMocks
    private PlanningService planningService;

    @Test
    void calculateRetirementReadiness_shouldCalculateCorrectly() {
        // Arrange
        int age = 35;
        BigDecimal desiredIncome = new BigDecimal("100000");
        BigDecimal currentSavings = new BigDecimal("50000");
        BigDecimal monthlyContribution = new BigDecimal("1000");

        // Act
        RetirementReadinessDto result = planningService.calculateRetirementReadiness(age, desiredIncome, currentSavings, monthlyContribution);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("2500000"), result.getRequiredNestEgg()); // 100k * 25
        assertTrue(result.getProjectedNestEgg().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.getScore() > 0);
    }
}
