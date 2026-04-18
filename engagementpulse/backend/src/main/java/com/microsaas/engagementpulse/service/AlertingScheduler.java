package com.microsaas.engagementpulse.service;

import com.microsaas.engagementpulse.model.Alert;
import com.microsaas.engagementpulse.model.SurveyResponse;
import com.microsaas.engagementpulse.repository.AlertRepository;
import com.microsaas.engagementpulse.repository.SurveyResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlertingScheduler {

    private final SurveyResponseRepository responseRepository;
    private final AlertRepository alertRepository;

    @Autowired
    public AlertingScheduler(SurveyResponseRepository responseRepository, AlertRepository alertRepository) {
        this.responseRepository = responseRepository;
        this.alertRepository = alertRepository;
    }

    // Runs every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void evaluateEngagementScores() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        List<SurveyResponse> recentResponses = responseRepository.findAll().stream()
                .filter(r -> r.getSubmittedAt().isAfter(start))
                .collect(Collectors.toList());

        // Group by Team and Tenant
        Map<String, List<SurveyResponse>> grouped = recentResponses.stream()
                .collect(Collectors.groupingBy(r -> r.getTenantId().toString() + "_" + r.getTeamId().toString()));

        for (Map.Entry<String, List<SurveyResponse>> entry : grouped.entrySet()) {
            List<SurveyResponse> teamResponses = entry.getValue();
            if (teamResponses.isEmpty()) continue;
            
            double average = teamResponses.stream()
                    .filter(r -> r.getEngagementScore() != null)
                    .mapToDouble(SurveyResponse::getEngagementScore)
                    .average()
                    .orElse(100.0);

            if (average < 60.0) {
                SurveyResponse sample = teamResponses.get(0);
                createAlert(sample.getTenantId(), sample.getTeamId(), "Engagement score dropped to " + average);
            }
        }
    }

    private void createAlert(UUID tenantId, UUID teamId, String message) {
        List<Alert> existing = alertRepository.findByTenantIdAndResolvedFalse(tenantId).stream()
                .filter(a -> a.getTeamId().equals(teamId))
                .collect(Collectors.toList());

        if (existing.isEmpty()) {
            Alert alert = new Alert();
            alert.setTenantId(tenantId);
            alert.setTeamId(teamId);
            alert.setSeverity("HIGH");
            alert.setMessage(message);
            alertRepository.save(alert);
        }
    }
}
