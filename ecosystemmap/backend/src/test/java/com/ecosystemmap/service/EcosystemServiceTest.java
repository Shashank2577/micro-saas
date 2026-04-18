package com.ecosystemmap.service;

import com.ecosystemmap.domain.DataFlow;
import com.ecosystemmap.domain.DeployedApp;
import com.ecosystemmap.dto.AppDto;
import com.ecosystemmap.dto.DataFlowDto;
import com.ecosystemmap.dto.EcosystemMapDto;
import com.ecosystemmap.repository.DataFlowRepository;
import com.ecosystemmap.repository.DeployedAppRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EcosystemServiceTest {

    @Mock
    private DeployedAppRepository deployedAppRepository;

    @Mock
    private DataFlowRepository dataFlowRepository;

    @InjectMocks
    private EcosystemService ecosystemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEcosystemMap() {
        UUID tenantId = UUID.randomUUID();
        when(deployedAppRepository.findByTenantId(tenantId)).thenReturn(List.of(new DeployedApp()));
        when(dataFlowRepository.findByTenantId(tenantId)).thenReturn(List.of(new DataFlow()));

        EcosystemMapDto result = ecosystemService.getEcosystemMap(tenantId);
        assertNotNull(result);
        assertEquals(1, result.getApps().size());
        assertEquals(1, result.getFlows().size());
    }

    @Test
    void testRegisterApp() {
        UUID tenantId = UUID.randomUUID();
        AppDto dto = new AppDto();
        dto.setAppId("app1");
        dto.setName("App 1");
        dto.setStatus("ACTIVE");

        when(deployedAppRepository.findByTenantIdAndAppId(tenantId, "app1")).thenReturn(Optional.empty());
        when(deployedAppRepository.save(any(DeployedApp.class))).thenAnswer(i -> i.getArguments()[0]);

        DeployedApp result = ecosystemService.registerApp(tenantId, dto);
        assertNotNull(result);
        assertEquals("app1", result.getAppId());
        assertEquals(tenantId, result.getTenantId());
    }

    @Test
    void testRecordDataFlow() {
        UUID tenantId = UUID.randomUUID();
        DataFlowDto dto = new DataFlowDto();
        dto.setSourceAppId(UUID.randomUUID());
        dto.setTargetAppId(UUID.randomUUID());
        dto.setEventType("event1");
        dto.setHealthStatus("HEALTHY");

        when(dataFlowRepository.save(any(DataFlow.class))).thenAnswer(i -> i.getArguments()[0]);

        DataFlow result = ecosystemService.recordDataFlow(tenantId, dto);
        assertNotNull(result);
        assertEquals("event1", result.getEventType());
        assertEquals(tenantId, result.getTenantId());
    }
}
