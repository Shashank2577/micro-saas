package com.microsaas.constructioniq.project;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SafetyIncidentServiceTest {

    @Mock
    private SafetyIncidentRepository incidentRepository;

    @InjectMocks
    private SafetyIncidentService incidentService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID projectId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void reportIncident_SetsProjectTenantAndTimestamps() {
        SafetyIncident incident = new SafetyIncident();
        incident.setDescription("Test Incident");
        incident.setIncidentDate(OffsetDateTime.now());

        when(incidentRepository.save(any(SafetyIncident.class))).thenAnswer(i -> i.getArguments()[0]);

        SafetyIncident created = incidentService.reportIncident(projectId, incident);

        assertEquals(projectId, created.getProjectId());
        assertEquals(tenantId, created.getTenantId());
        assertNotNull(created.getCreatedAt());
        verify(incidentRepository).save(incident);
    }
}
