package com.microsaas.incidentbrain.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.incidentbrain.domain.model.Incident;
import com.microsaas.incidentbrain.domain.model.RootCauseCandidate;
import com.microsaas.incidentbrain.domain.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final ChatClient chatClient;
    private final DatadogIntegrationService datadogService;
    private final GitHubIntegrationService githubService;

    public Incident createIncident(Incident incident) {
        String tenantId = TenantContext.require().toString();
        incident.setTenantId(tenantId);
        incident.setStatus("OPEN");
        if (incident.getTimelineEvents() == null) {
            incident.setTimelineEvents(new ArrayList<>());
        }
        return incidentRepository.save(incident);
    }

    public List<Incident> listIncidents() {
        return incidentRepository.findByTenantId(TenantContext.require().toString());
    }

    public Optional<Incident> getIncident(String id) {
        return incidentRepository.findByIdAndTenantId(id, TenantContext.require().toString());
    }

    public void ingestDatadogLogs(String incidentId, String serviceName) {
        Incident incident = incidentRepository.findByIdAndTenantId(incidentId, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        
        List<String> logs = datadogService.fetchRecentLogs(serviceName);
        for (String logEntry : logs) {
            Incident.TimelineEvent event = Incident.TimelineEvent.builder()
                    .timestamp(Instant.now())
                    .source("Datadog")
                    .type("LOG")
                    .summary(logEntry)
                    .build();
            incident.getTimelineEvents().add(event);
        }
        
        incidentRepository.save(incident);
    }

    public RootCauseCandidate analyzeIncident(String incidentId) {
        Incident incident = incidentRepository.findByIdAndTenantId(incidentId, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        String repo = "freestack/cross-cutting";
        if (incident.getTitle() != null && incident.getTitle().contains("/")) {
             repo = incident.getTitle().split(" ")[0];
        }
        
        String recentCommits = githubService.fetchRecentCommits(repo);

        String prompt = "Analyze the following incident and timeline events to determine the root cause:\n" +
                "Title: " + incident.getTitle() + "\n" +
                "Recent Commits: " + recentCommits + "\n" +
                "Timeline: " + incident.getTimelineEvents().toString() + "\n\n" +
                "Return the response as a JSON object with fields: description (string), confidence (number between 0 and 1), evidence (array of strings), and aiReasoning (string).";

        RootCauseCandidate candidate = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(new ParameterizedTypeReference<RootCauseCandidate>() {});
        
        incident.setRootCauseHypothesis(candidate.getDescription());
        incident.setConfidenceScore(candidate.getConfidence());
        incidentRepository.save(incident);
        
        return candidate;
    }

    public String generatePostmortem(String incidentId) {
        Incident incident = incidentRepository.findByIdAndTenantId(incidentId, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        String prompt = "Generate a postmortem markdown document for the following incident:\n" +
                "Title: " + incident.getTitle() + "\n" +
                "Root Cause: " + incident.getRootCauseHypothesis() + "\n" +
                "Timeline: " + incident.getTimelineEvents().toString();

        String postmortem = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        incident.setPostmortemDraft(postmortem);
        incidentRepository.save(incident);

        return postmortem;
    }
}
