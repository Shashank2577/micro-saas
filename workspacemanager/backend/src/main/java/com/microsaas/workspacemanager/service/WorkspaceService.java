package com.microsaas.workspacemanager.service;

import com.microsaas.workspacemanager.domain.Workspace;
import com.microsaas.workspacemanager.domain.SsoDomain;
import com.microsaas.workspacemanager.dto.WorkspaceDto;
import com.microsaas.workspacemanager.dto.SsoDomainDto;
import com.microsaas.workspacemanager.repository.WorkspaceRepository;
import com.microsaas.workspacemanager.repository.SsoDomainRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final SsoDomainRepository ssoDomainRepository;
    private final AuditService auditService;
    private final EventPublisher eventPublisher;
    private final StringRedisTemplate redisTemplate;

    public Workspace createWorkspace(WorkspaceDto dto) {
        UUID tenantId = TenantContext.require();
        Workspace workspace = new Workspace();
        workspace.setTenantId(tenantId);
        workspace.setName(dto.getName());
        workspace.setSubdomain(dto.getSubdomain());
        workspace.setStatus("ACTIVE");
        workspace.setCapacityLimit(100);
        workspace.setFeatures(dto.getFeatures());
        workspace.setBrandingLogoUrl(dto.getBrandingLogoUrl());
        workspace = workspaceRepository.save(workspace);
        
        auditService.logAction(tenantId, "WORKSPACE_CREATED", null, workspace.getId(), "{}");
        eventPublisher.publishEvent("WORKSPACE_CREATED", workspace);
        
        // Cache context
        redisTemplate.opsForValue().set("workspace:context:" + tenantId, workspace.getId().toString());

        return workspace;
    }

    public Workspace getWorkspace() {
        UUID tenantId = TenantContext.require();
        
        String cachedId = redisTemplate.opsForValue().get("workspace:context:" + tenantId);
        if (cachedId != null) {
            System.out.println("Cache hit for workspace");
        }

        return workspaceRepository.findByTenantId(tenantId).orElseThrow();
    }

    public Workspace updateWorkspace(WorkspaceDto dto) {
        Workspace workspace = getWorkspace();
        workspace.setName(dto.getName());
        workspace.setSubdomain(dto.getSubdomain());
        workspace.setBrandingLogoUrl(dto.getBrandingLogoUrl());
        workspace.setFeatures(dto.getFeatures());
        workspace = workspaceRepository.save(workspace);

        auditService.logAction(workspace.getTenantId(), "WORKSPACE_UPDATED", null, workspace.getId(), "{}");
        return workspace;
    }

    public Workspace suspendWorkspace() {
        Workspace workspace = getWorkspace();
        workspace.setStatus("SUSPENDED");
        workspace = workspaceRepository.save(workspace);
        
        auditService.logAction(workspace.getTenantId(), "WORKSPACE_SUSPENDED", null, workspace.getId(), "{}");
        return workspace;
    }

    public List<SsoDomain> getSsoDomains() {
        return ssoDomainRepository.findByTenantId(TenantContext.require());
    }

    public SsoDomain addSsoDomain(SsoDomainDto dto) {
        UUID tenantId = TenantContext.require();
        SsoDomain domain = new SsoDomain();
        domain.setTenantId(tenantId);
        domain.setDomain(dto.getDomain());
        domain.setVerified(true);
        domain = ssoDomainRepository.save(domain);

        auditService.logAction(tenantId, "SSO_DOMAIN_ADDED", null, domain.getId(), "{\"domain\":\"" + dto.getDomain() + "\"}");
        return domain;
    }
}
