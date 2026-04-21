package com.microsaas.constructioniq.project;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SafetyIncidentService {

    private final SafetyIncidentRepository incidentRepository;

    public SafetyIncident reportIncident(UUID projectId, SafetyIncident incident) {
        incident.setProjectId(projectId);
        incident.setTenantId(TenantContext.require());
        incident.setCreatedAt(OffsetDateTime.now());
        incident.setUpdatedAt(OffsetDateTime.now());
        return incidentRepository.save(incident);
    }

    @Transactional(readOnly = true)
    public List<SafetyIncident> getIncidents(UUID projectId) {
        return incidentRepository.findByProjectIdAndTenantId(projectId, TenantContext.require());
    }

    @Transactional(readOnly = true)
    public SafetyIncident getIncident(UUID id) {
        return incidentRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Safety incident not found"));
    }

    public SafetyIncident updateIncident(UUID id, SafetyIncident incidentUpdate) {
        SafetyIncident incident = getIncident(id);
        incident.setDescription(incidentUpdate.getDescription());
        incident.setSeverity(incidentUpdate.getSeverity());
        incident.setStatus(incidentUpdate.getStatus());
        incident.setUpdatedAt(OffsetDateTime.now());
        return incidentRepository.save(incident);
    }
}
