package com.microsaas.datalineagetracker.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.datalineagetracker.dto.AuditEvaluationRequest;
import com.microsaas.datalineagetracker.entity.AuditLog;
import com.microsaas.datalineagetracker.entity.DataAsset;
import com.microsaas.datalineagetracker.entity.Incident;
import com.microsaas.datalineagetracker.repository.AuditLogRepository;
import com.microsaas.datalineagetracker.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    private final IncidentRepository incidentRepository;
    private final DataAssetService assetService;

    @Transactional(readOnly = true)
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAllByTenantId(TenantContext.require());
    }

    @Transactional
    public AuditLog evaluateAccess(AuditEvaluationRequest request) {
        DataAsset asset = assetService.getAssetById(request.getAssetId());
        
        AuditLog log = new AuditLog();
        log.setTenantId(TenantContext.require());
        log.setAsset(asset);
        log.setUserId(request.getUserId());
        log.setAction(request.getAction());

        // Basic mock policy evaluation logic
        if ("RESTRICTED".equals(asset.getClassification()) && "EXPORT".equals(request.getAction())) {
            log.setStatus("DENIED");
            log.setReason("DLP rule blocked export of RESTRICTED asset.");
            
            // Generate incident on blocked export
            createIncident(asset, "UNAUTHORIZED_ACCESS", "Attempted export of RESTRICTED asset by user " + request.getUserId());
        } else {
            log.setStatus("ALLOWED");
            log.setReason("Passed standard access checks.");
        }

        return auditLogRepository.save(log);
    }

    private void createIncident(DataAsset asset, String type, String description) {
        Incident incident = new Incident();
        incident.setTenantId(TenantContext.require());
        incident.setAsset(asset);
        incident.setType(type);
        incident.setStatus("OPEN");
        incident.setDescription(description);
        incidentRepository.save(incident);
    }
}
