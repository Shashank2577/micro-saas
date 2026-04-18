package com.tenantmanager.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.tenantmanager.domain.CustomerTenant;
import com.tenantmanager.domain.OnboardingMilestone;
import com.tenantmanager.domain.TenantEvent;
import com.tenantmanager.dto.CreateEventRequest;
import com.tenantmanager.dto.CreateMilestoneRequest;
import com.tenantmanager.dto.CreateTenantRequest;
import com.tenantmanager.repository.CustomerTenantRepository;
import com.tenantmanager.repository.OnboardingMilestoneRepository;
import com.tenantmanager.repository.TenantEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final CustomerTenantRepository tenantRepository;
    private final OnboardingMilestoneRepository milestoneRepository;
    private final TenantEventRepository eventRepository;
    private final AiHealthService aiHealthService;

    public List<CustomerTenant> getTenants() {
        return tenantRepository.findByTenantId(TenantContext.require());
    }

    @Transactional
    public CustomerTenant createTenant(CreateTenantRequest request) {
        CustomerTenant tenant = CustomerTenant.builder()
                .tenantId(TenantContext.require())
                .name(request.getName())
                .status("ONBOARDING")
                .build();
        return tenantRepository.save(tenant);
    }

    public CustomerTenant getTenant(UUID id) {
        return tenantRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
    }

    @Transactional
    public CustomerTenant updateStatus(UUID id, String status) {
        CustomerTenant tenant = getTenant(id);
        tenant.setStatus(status);
        return tenantRepository.save(tenant);
    }

    public List<OnboardingMilestone> getMilestones(UUID tenantId) {
        return milestoneRepository.findByCustomerTenantIdAndTenantId(tenantId, TenantContext.require());
    }

    @Transactional
    public OnboardingMilestone createMilestone(UUID tenantId, CreateMilestoneRequest request) {
        OnboardingMilestone milestone = OnboardingMilestone.builder()
                .tenantId(TenantContext.require())
                .customerTenantId(tenantId)
                .title(request.getTitle())
                .description(request.getDescription())
                .status("PENDING")
                .build();
        return milestoneRepository.save(milestone);
    }

    @Transactional
    public OnboardingMilestone completeMilestone(UUID milestoneId) {
        OnboardingMilestone milestone = milestoneRepository.findByIdAndTenantId(milestoneId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Milestone not found"));
        milestone.setStatus("COMPLETED");
        milestone.setCompletedAt(LocalDateTime.now());
        return milestoneRepository.save(milestone);
    }

    public List<TenantEvent> getEvents(UUID tenantId) {
        return eventRepository.findByCustomerTenantIdAndTenantIdOrderByOccurredAtDesc(tenantId, TenantContext.require());
    }

    @Transactional
    public TenantEvent createEvent(UUID tenantId, CreateEventRequest request) {
        TenantEvent event = TenantEvent.builder()
                .tenantId(TenantContext.require())
                .customerTenantId(tenantId)
                .eventType(request.getEventType())
                .description(request.getDescription())
                .build();
        return eventRepository.save(event);
    }

    @Transactional
    public CustomerTenant analyzeHealth(UUID id) {
        CustomerTenant tenant = getTenant(id);
        List<TenantEvent> events = getEvents(id);

        AiHealthService.HealthAnalysis analysis = aiHealthService.analyzeHealth(tenant, events);

        tenant.setHealthScore(analysis.getHealthScore());
        tenant.setChurnRisk(analysis.getChurnRisk());

        return tenantRepository.save(tenant);
    }
}
