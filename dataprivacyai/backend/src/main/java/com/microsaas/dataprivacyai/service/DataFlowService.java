package com.microsaas.dataprivacyai.service;

import com.microsaas.dataprivacyai.domain.*;
import com.microsaas.dataprivacyai.repository.DataFlowRepository;
import com.microsaas.dataprivacyai.repository.PrivacyRiskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataFlowService {
    private final DataFlowRepository dataFlowRepository;
    private final PrivacyRiskRepository privacyRiskRepository;

    @Transactional
    public DataFlow addDataFlow(String sourceSystem, String destinationSystem, DataCategory dataCategory,
                                String transferMechanism, LegalBasis legalBasis, UUID tenantId) {
        DataFlow flow = new DataFlow();
        flow.setSourceSystem(sourceSystem);
        flow.setDestinationSystem(destinationSystem);
        flow.setDataCategory(dataCategory);
        flow.setTransferMechanism(transferMechanism);
        flow.setLegalBasis(legalBasis);
        flow.setTenantId(tenantId);
        return dataFlowRepository.save(flow);
    }

    public List<DataFlow> listDataFlows(UUID tenantId) {
        return dataFlowRepository.findByTenantId(tenantId);
    }

    @Transactional
    public void assessRisks(UUID dataFlowId, UUID tenantId) {
        Optional<DataFlow> optionalFlow = dataFlowRepository.findById(dataFlowId);
        if (optionalFlow.isEmpty() || !optionalFlow.get().getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("DataFlow not found or unauthorized");
        }

        DataFlow flow = optionalFlow.get();

        if (flow.getLegalBasis() == null) {
            PrivacyRisk risk = new PrivacyRisk();
            risk.setDataFlowId(flow.getId());
            risk.setRiskType(RiskType.MISSING_LEGAL_BASIS);
            risk.setSeverity(RiskSeverity.HIGH);
            risk.setDescription("Data flow is missing a legal basis for processing.");
            risk.setStatus(RiskStatus.OPEN);
            risk.setTenantId(tenantId);
            privacyRiskRepository.save(risk);
        }

        if ((flow.getDataCategory() == DataCategory.HEALTH || flow.getDataCategory() == DataCategory.SENSITIVE)
                && flow.getLegalBasis() != LegalBasis.CONSENT) {
            PrivacyRisk risk = new PrivacyRisk();
            risk.setDataFlowId(flow.getId());
            risk.setRiskType(RiskType.MISSING_LEGAL_BASIS);
            risk.setSeverity(RiskSeverity.CRITICAL);
            risk.setDescription("Sensitive or health data requires explicit consent.");
            risk.setStatus(RiskStatus.OPEN);
            risk.setTenantId(tenantId);
            privacyRiskRepository.save(risk);
        }
    }
}
