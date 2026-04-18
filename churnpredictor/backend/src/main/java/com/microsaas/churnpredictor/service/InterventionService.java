package com.microsaas.churnpredictor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.churnpredictor.dto.InterventionDto;
import com.microsaas.churnpredictor.dto.InterventionPlaybookDto;
import com.microsaas.churnpredictor.entity.Customer;
import com.microsaas.churnpredictor.entity.CustomerHealthScore;
import com.microsaas.churnpredictor.entity.Intervention;
import com.microsaas.churnpredictor.entity.InterventionPlaybook;
import com.microsaas.churnpredictor.repository.CustomerHealthScoreRepository;
import com.microsaas.churnpredictor.repository.CustomerRepository;
import com.microsaas.churnpredictor.repository.InterventionPlaybookRepository;
import com.microsaas.churnpredictor.repository.InterventionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterventionService {
    private final InterventionRepository interventionRepository;
    private final InterventionPlaybookRepository playbookRepository;
    private final CustomerRepository customerRepository;
    private final CustomerHealthScoreRepository healthScoreRepository;
    private final AiOfferService aiOfferService;

    @Transactional(readOnly = true)
    public List<InterventionPlaybookDto> getPlaybooks() {
        UUID tenantId = TenantContext.require();
        return playbookRepository.findByTenantId(tenantId)
                .stream().map(this::mapPlaybookToDto).collect(Collectors.toList());
    }

    @Transactional
    public InterventionPlaybookDto createPlaybook(InterventionPlaybookDto dto) {
        UUID tenantId = TenantContext.require();
        InterventionPlaybook playbook = new InterventionPlaybook();
        playbook.setTenantId(tenantId);
        playbook.setName(dto.getName());
        playbook.setDescription(dto.getDescription());
        playbook.setTriggerRiskSegment(dto.getTriggerRiskSegment());
        playbook.setActionType(dto.getActionType());
        playbook.setActive(dto.getActive() != null ? dto.getActive() : true);
        playbook = playbookRepository.save(playbook);
        return mapPlaybookToDto(playbook);
    }

    @Transactional(readOnly = true)
    public List<InterventionDto> getInterventions() {
        UUID tenantId = TenantContext.require();
        return interventionRepository.findByTenantId(tenantId)
                .stream().map(this::mapInterventionToDto).collect(Collectors.toList());
    }

    @Transactional
    public InterventionDto generateOffer(UUID interventionId) {
        UUID tenantId = TenantContext.require();
        Intervention intervention = interventionRepository.findByIdAndTenantId(interventionId, tenantId)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        Customer customer = intervention.getCustomer();
        InterventionPlaybook playbook = intervention.getPlaybook();
        CustomerHealthScore healthScore = healthScoreRepository.findFirstByTenantIdAndCustomerIdOrderByCalculatedAtDesc(tenantId, customer.getId()).orElse(null);

        String offerText = aiOfferService.generateOffer(customer, healthScore, playbook);
        intervention.setOfferDetails(offerText);
        intervention.setStatus("COMPLETED");
        intervention = interventionRepository.save(intervention);
        
        return mapInterventionToDto(intervention);
    }

    private InterventionPlaybookDto mapPlaybookToDto(InterventionPlaybook entity) {
        InterventionPlaybookDto dto = new InterventionPlaybookDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setTriggerRiskSegment(entity.getTriggerRiskSegment());
        dto.setActionType(entity.getActionType());
        dto.setActive(entity.getActive());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private InterventionDto mapInterventionToDto(Intervention entity) {
        InterventionDto dto = new InterventionDto();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomer().getId());
        dto.setPlaybookId(entity.getPlaybook().getId());
        dto.setStatus(entity.getStatus());
        dto.setOfferDetails(entity.getOfferDetails());
        dto.setEffectivenessStatus(entity.getEffectivenessStatus());
        dto.setExecutedAt(entity.getExecutedAt());
        return dto;
    }
}
