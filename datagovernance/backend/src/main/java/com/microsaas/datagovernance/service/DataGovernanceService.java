package com.microsaas.datagovernance.service;

import com.microsaas.datagovernance.model.*;
import com.microsaas.datagovernance.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DataGovernanceService {

    @Autowired
    private DataRetentionPolicyRepository policyRepository;

    @Autowired
    private DataSubjectRequestRepository dsarRepository;

    @Autowired
    private ConsentRecordRepository consentRepository;

    @Autowired
    private DataLineageNodeRepository lineageRepository;

    @Autowired
    private AuditLogRepository auditRepository;

    private UUID getTenantId() {
        return TenantContext.require();
    }

    public List<DataRetentionPolicy> getPolicies() {
        return policyRepository.findByTenantId(getTenantId());
    }

    public DataRetentionPolicy createPolicy(DataRetentionPolicy policy) {
        policy.setId(UUID.randomUUID());
        policy.setTenantId(getTenantId());
        policy.setCreatedAt(LocalDateTime.now());
        policy.setUpdatedAt(LocalDateTime.now());
        return policyRepository.save(policy);
    }

    public void deletePolicy(UUID id) {
        policyRepository.deleteById(id);
    }

    public List<DataSubjectRequest> getDsars() {
        return dsarRepository.findByTenantId(getTenantId());
    }

    public DataSubjectRequest getDsar(UUID id) { return dsarRepository.findById(id).orElseThrow(); }

    public DataSubjectRequest createDsar(DataSubjectRequest request) {
        request.setId(UUID.randomUUID());
        request.setTenantId(getTenantId());
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());
        return dsarRepository.save(request);
    }

    public DataSubjectRequest processDsar(UUID id) {
        DataSubjectRequest request = dsarRepository.findById(id).orElseThrow();
        request.setStatus("COMPLETED");
        request.setCompletedAt(LocalDateTime.now());
        return dsarRepository.save(request);
    }

    public List<ConsentRecord> getConsent(String email) {
        return consentRepository.findByTenantIdAndUserEmail(getTenantId(), email);
    }

    public ConsentRecord createConsent(ConsentRecord consent) {
        consent.setId(UUID.randomUUID());
        consent.setTenantId(getTenantId());
        consent.setTimestamp(LocalDateTime.now());
        return consentRepository.save(consent);
    }

    public List<DataLineageNode> getLineage(String fieldName) {
        return lineageRepository.findByTenantIdAndFieldName(getTenantId(), fieldName);
    }

    public List<AuditLog> getAuditLogs() {
        return auditRepository.findByTenantId(getTenantId());
    }
}
