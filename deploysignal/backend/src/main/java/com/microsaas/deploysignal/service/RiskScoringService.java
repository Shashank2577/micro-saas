package com.microsaas.deploysignal.service;

import org.springframework.stereotype.Service;

@Service
public class RiskScoringService {

    public int calculateRiskScore(int filesChanged, boolean testsPassing, int timeSinceLastDeployMinutes) {
        int score = 0;
        
        if (filesChanged > 100) {
            score += 50;
        } else if (filesChanged > 50) {
            score += 20;
        } else if (filesChanged > 10) {
            score += 5;
        }
        
        if (!testsPassing) {
            score += 100; // Critical risk
        }
        
        if (timeSinceLastDeployMinutes < 60) {
            score += 10;
        }
        
        return Math.min(score, 100);
    }
}
