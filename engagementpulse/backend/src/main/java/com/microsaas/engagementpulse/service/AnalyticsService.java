package com.microsaas.engagementpulse.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.engagementpulse.model.SurveyResponse;
import com.microsaas.engagementpulse.repository.SurveyResponseRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final SurveyResponseRepository responseRepository;
    private final EntityManager entityManager;

    @Autowired
    public AnalyticsService(SurveyResponseRepository responseRepository, EntityManager entityManager) {
        this.responseRepository = responseRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getTeamAnalytics(UUID teamId) {
        UUID tenantId = TenantContext.require();
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
        List<SurveyResponse> responses = responseRepository.findByTeamIdAndTenantId(teamId, tenantId);

        double totalScore = 0;
        int count = 0;

        for (SurveyResponse r : responses) {
            if (r.getEngagementScore() != null) {
                totalScore += r.getEngagementScore();
                count++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("teamId", teamId);
        result.put("averageEngagementScore", count > 0 ? totalScore / count : 0);
        result.put("responseCount", responses.size());
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getTrends() {
        UUID tenantId = TenantContext.require();
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(30);

        List<SurveyResponse> responses = responseRepository.findAllByTenantIdAndSubmittedAtBetween(tenantId, start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("totalResponsesInLast30Days", responses.size());
        
        // Group by day for simple trend
        Map<String, Long> responsesPerDay = responses.stream()
            .collect(Collectors.groupingBy(r -> r.getSubmittedAt().toLocalDate().toString(), Collectors.counting()));
            
        result.put("responsesPerDay", responsesPerDay);
        return result;
    }

    @Transactional(readOnly = true)
    public String generateExportData() {
        UUID tenantId = TenantContext.require();
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);

        List<SurveyResponse> responses = responseRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("Response ID,Team ID,Employee ID,Submitted At,Engagement Score\n");
        for (SurveyResponse r : responses) {
            sb.append(r.getId()).append(",")
              .append(r.getTeamId()).append(",")
              .append(r.getEmployeeId()).append(",")
              .append(r.getSubmittedAt()).append(",")
              .append(r.getEngagementScore()).append("\n");
        }
        return sb.toString();
    }
}
