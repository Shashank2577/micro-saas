package com.microsaas.incidentbrain;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.incidentbrain.domain.model.Incident;
import com.microsaas.incidentbrain.domain.repository.IncidentRepository;
import com.microsaas.incidentbrain.service.DatadogIntegrationService;
import com.microsaas.incidentbrain.service.GitHubIntegrationService;
import com.microsaas.incidentbrain.service.IncidentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IncidentServiceTest {

    private IncidentService incidentService;
    private IncidentRepository incidentRepository;
    private ChatClient chatClient;
    private DatadogIntegrationService datadogService;
    private GitHubIntegrationService githubService;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        incidentRepository = mock(IncidentRepository.class);
        chatClient = mock(ChatClient.class);
        datadogService = mock(DatadogIntegrationService.class);
        githubService = mock(GitHubIntegrationService.class);
        incidentService = new IncidentService(incidentRepository, chatClient, datadogService, githubService);
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateIncident() {
        Incident incident = new Incident();
        incident.setTitle("Database down");

        when(incidentRepository.save(any(Incident.class))).thenAnswer(i -> {
            Incident arg = i.getArgument(0);
            arg.setId("test-id");
            return arg;
        });

        Incident created = incidentService.createIncident(incident);
        assertEquals("OPEN", created.getStatus());
        assertEquals("test-id", created.getId());
        assertEquals(tenantId.toString(), created.getTenantId());
    }
}
