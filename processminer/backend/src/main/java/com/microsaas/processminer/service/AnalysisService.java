package com.microsaas.processminer.service;

import com.microsaas.processminer.domain.*;
import com.microsaas.processminer.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AnalysisService {
    
    private final AnalysisResultRepository analysisResultRepository;
    private final AutomationOpportunityRepository opportunityRepository;
    private final ProcessModelRepository processModelRepository;
    private final EventLogRepository eventLogRepository;
    private final PolicyRepository policyRepository;
    private final AIAnalysisService aiAnalysisService;
    private final ObjectMapper objectMapper;

    public AnalysisService(
            AnalysisResultRepository analysisResultRepository,
            AutomationOpportunityRepository opportunityRepository,
            ProcessModelRepository processModelRepository,
            EventLogRepository eventLogRepository,
            PolicyRepository policyRepository,
            AIAnalysisService aiAnalysisService,
            ObjectMapper objectMapper) {
        this.analysisResultRepository = analysisResultRepository;
        this.opportunityRepository = opportunityRepository;
        this.processModelRepository = processModelRepository;
        this.eventLogRepository = eventLogRepository;
        this.policyRepository = policyRepository;
        this.aiAnalysisService = aiAnalysisService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void analyzeProcessModel(UUID processModelId) {
        UUID tenantId = TenantContext.require();
        ProcessModel model = processModelRepository.findByIdAndTenantId(processModelId, tenantId)
            .orElseThrow(() -> new RuntimeException("Model not found"));
            
        // Clean existing
        analysisResultRepository.deleteByTenantIdAndProcessModelId(tenantId, processModelId);
        opportunityRepository.deleteByTenantIdAndProcessModelId(tenantId, processModelId);

        List<EventLog> events = eventLogRepository.findByTenantIdAndSystemTypeOrderByTimestampAsc(tenantId, model.getSystemType());

        // 1. Simulate Bottleneck Analysis
        AnalysisResult bottleneck = new AnalysisResult();
        bottleneck.setTenantId(tenantId);
        bottleneck.setProcessModelId(processModelId);
        bottleneck.setType("BOTTLENECK");
        bottleneck.setSeverity("HIGH");
        bottleneck.setDescription("Review stage takes on average 4.5 days");
        analysisResultRepository.save(bottleneck);

        // 2. Simulate Compliance Checks
        List<Policy> policies = policyRepository.findByTenantIdAndProcessModelId(tenantId, processModelId);
        for(Policy policy : policies) {
            AnalysisResult gap = new AnalysisResult();
            gap.setTenantId(tenantId);
            gap.setProcessModelId(processModelId);
            gap.setType("COMPLIANCE_GAP");
            gap.setSeverity("MEDIUM");
            gap.setDescription("Deviation from policy: " + policy.getName());
            analysisResultRepository.save(gap);
        }

        // 3. Automation Opportunities
        List<String> activities = events.stream()
                .map(EventLog::getActivityName)
                .distinct()
                .toList();

        for (String activity : activities) {
            if (activity.toLowerCase().contains("manual") || activity.toLowerCase().contains("review")) {
                AIAnalysisService.OpportunityScore score = aiAnalysisService.analyzeOpportunity(activity, model.getDescription());
                
                AutomationOpportunity opp = new AutomationOpportunity();
                opp.setTenantId(tenantId);
                opp.setProcessModelId(processModelId);
                opp.setActivityName(activity);
                opp.setEstimatedRoi(score.roi());
                opp.setEffortEstimate(score.effort());
                opp.setRationale(score.rationale());
                opportunityRepository.save(opp);
            }
        }
    }
    
    public List<AnalysisResult> getAnalysisResults(UUID processModelId) {
        return analysisResultRepository.findByTenantIdAndProcessModelId(TenantContext.require(), processModelId);
    }
    
    public List<AutomationOpportunity> getAutomationOpportunities() {
        return opportunityRepository.findByTenantId(TenantContext.require());
    }
}
