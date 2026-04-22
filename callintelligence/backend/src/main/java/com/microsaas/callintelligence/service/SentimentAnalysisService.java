package com.microsaas.callintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.callintelligence.domain.insight.SentimentAnalysis;
import com.microsaas.callintelligence.domain.insight.SentimentAnalysisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SentimentAnalysisService {

    private final SentimentAnalysisRepository sentimentAnalysisRepository;

    public SentimentAnalysisService(SentimentAnalysisRepository sentimentAnalysisRepository) {
        this.sentimentAnalysisRepository = sentimentAnalysisRepository;
    }

    @Transactional(readOnly = true)
    public List<SentimentAnalysis> getSentimentForCall(UUID callId) {
        UUID tenantId = TenantContext.require();
        return sentimentAnalysisRepository.findByCallIdAndTenantId(callId, tenantId);
    }
}
