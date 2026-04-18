package com.ecosystemmap.service;

import com.ecosystemmap.domain.DataFlow;
import com.ecosystemmap.domain.DeployedApp;
import com.ecosystemmap.dto.AppDto;
import com.ecosystemmap.dto.DataFlowDto;
import com.ecosystemmap.dto.EcosystemMapDto;
import com.ecosystemmap.repository.DataFlowRepository;
import com.ecosystemmap.repository.DeployedAppRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service

public class EcosystemService {

    private DeployedAppRepository deployedAppRepository;
    private DataFlowRepository dataFlowRepository;

    public EcosystemMapDto getEcosystemMap(UUID tenantId) {
        EcosystemMapDto dto = new EcosystemMapDto();
        dto.setApps(deployedAppRepository.findByTenantId(tenantId));
        dto.setFlows(dataFlowRepository.findByTenantId(tenantId));
        return dto;
    }

    @Transactional
    public DeployedApp registerApp(UUID tenantId, AppDto appDto) {
        DeployedApp app = deployedAppRepository.findByTenantIdAndAppId(tenantId, appDto.getAppId())
                .orElse(new DeployedApp());
        
        if (app.getId() == null) {
            app.setId(UUID.randomUUID());
            app.setTenantId(tenantId);
            app.setDeployedAt(LocalDateTime.now());
            app.setCreatedAt(LocalDateTime.now());
        }
        
        app.setTenantId(tenantId);
        app.setAppId(appDto.getAppId());
        app.setName(appDto.getName());
        app.setStatus(appDto.getStatus());
        app.setUpdatedAt(LocalDateTime.now());
        
        return deployedAppRepository.save(app);
    }

    @Transactional
    public DataFlow recordDataFlow(UUID tenantId, DataFlowDto flowDto) {
        DataFlow flow = new DataFlow();
        flow.setId(UUID.randomUUID());
        flow.setTenantId(tenantId);
        flow.setSourceAppId(flowDto.getSourceAppId());
        flow.setTargetAppId(flowDto.getTargetAppId());
        flow.setEventType(flowDto.getEventType());
        flow.setHealthStatus(flowDto.getHealthStatus());
        flow.setLastEventAt(LocalDateTime.now());
        flow.setCreatedAt(LocalDateTime.now());
        flow.setUpdatedAt(LocalDateTime.now());
        
        return dataFlowRepository.save(flow);
    }
}
