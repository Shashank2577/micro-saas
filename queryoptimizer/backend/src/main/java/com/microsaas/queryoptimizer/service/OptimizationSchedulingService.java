package com.microsaas.queryoptimizer.service;

import com.microsaas.queryoptimizer.domain.QueryFingerprint;
import com.microsaas.queryoptimizer.repository.QueryFingerprintRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptimizationSchedulingService {

    private final QueryFingerprintRepository fingerprintRepository;
    private final AIRecommendationService aiRecommendationService;

    public OptimizationSchedulingService(QueryFingerprintRepository fingerprintRepository, AIRecommendationService aiRecommendationService) {
        this.fingerprintRepository = fingerprintRepository;
        this.aiRecommendationService = aiRecommendationService;
    }

    // Schedule to run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleAutomaticOptimization() {
        System.out.println("Running scheduled optimization analysis...");
        // In a real multi-tenant system, we would iterate tenants. 
        // For simplicity, we just fetch all fingerprints here and process them.
        int page = 0;
        int size = 100;
        Page<QueryFingerprint> fingerprints;
        do {
            fingerprints = fingerprintRepository.findAll(PageRequest.of(page, size));
            for (QueryFingerprint fingerprint : fingerprints) {
                try {
                    aiRecommendationService.analyzeFingerprint(fingerprint.getTenantId(), fingerprint.getId());
                } catch (Exception e) {
                    System.err.println("Failed auto analysis for fingerprint " + fingerprint.getId());
                }
            }
            page++;
        } while (fingerprints.hasNext());
    }
}
