package com.microsaas.deploysignal.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RiskScoringServiceTest {

    private final RiskScoringService service = new RiskScoringService();

    @Test
    public void testHighRiskDeploy() {
        int score = service.calculateRiskScore(150, false, 30);
        assertEquals(100, score);
    }
    
    @Test
    public void testLowRiskDeploy() {
        int score = service.calculateRiskScore(5, true, 120);
        assertEquals(0, score);
    }
    
    @Test
    public void testMediumRiskDeploy() {
        int score = service.calculateRiskScore(60, true, 45);
        assertEquals(30, score); // 20 from files + 10 from time
    }
}
