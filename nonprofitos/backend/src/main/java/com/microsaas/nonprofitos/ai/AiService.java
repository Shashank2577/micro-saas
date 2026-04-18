package com.microsaas.nonprofitos.ai;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    public String getDonorIntelligence(String name, java.math.BigDecimal totalGiven) {
        // Mocking the AI response for simplicity and to avoid external dependency in tests
        return "Donor " + name + " has given $" + totalGiven + ". High upgrade potential.";
    }

    public String generateGrantDraft(String title, String funder) {
        // Mocking the AI response
        return "Draft proposal for " + title + " to " + funder + ".\n\nWe propose...";
    }

    public String generateImpactNarrative(String metrics) {
        // Mocking the AI response
        return "Based on the recent metrics:\n" + metrics + "\n\nWe have made significant progress.";
    }
}
