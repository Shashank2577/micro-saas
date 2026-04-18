package com.microsaas.workspacemanager.service;

import com.microsaas.workspacemanager.domain.Workspace;
import com.microsaas.workspacemanager.dto.WorkspaceDto;
import com.microsaas.workspacemanager.repository.WorkspaceRepository;
import com.microsaas.workspacemanager.repository.SsoDomainRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;
    
    @Mock
    private SsoDomainRepository ssoDomainRepository;

    @Mock
    private AuditService auditService;
    
    @Mock
    private EventPublisher eventPublisher;
    
    @Mock
    private StringRedisTemplate redisTemplate;
    
    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private WorkspaceService workspaceService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateWorkspace() {
        WorkspaceDto dto = new WorkspaceDto();
        dto.setName("Test Workspace");
        dto.setSubdomain("test");
        
        Workspace workspace = new Workspace();
        workspace.setId(UUID.randomUUID());
        workspace.setTenantId(tenantId);
        workspace.setName("Test Workspace");
        workspace.setSubdomain("test");
        
        when(workspaceRepository.save(any())).thenReturn(workspace);
        doNothing().when(auditService).logAction(any(), any(), any(), any(), any());
        doNothing().when(eventPublisher).publishEvent(anyString(), any());
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(anyString(), anyString());

        Workspace result = workspaceService.createWorkspace(dto);

        assertNotNull(result);
        assertEquals("Test Workspace", result.getName());
        verify(workspaceRepository, times(1)).save(any());
        verify(auditService, times(1)).logAction(eq(tenantId), eq("WORKSPACE_CREATED"), isNull(), eq(workspace.getId()), anyString());
        verify(eventPublisher, times(1)).publishEvent(eq("WORKSPACE_CREATED"), eq(workspace));
        verify(valueOperations, times(1)).set(eq("workspace:context:" + tenantId), eq(workspace.getId().toString()));
    }

    @Test
    void testGetWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setTenantId(tenantId);
        when(workspaceRepository.findByTenantId(tenantId)).thenReturn(Optional.of(workspace));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(null);

        Workspace result = workspaceService.getWorkspace();
        
        assertNotNull(result);
        verify(workspaceRepository, times(1)).findByTenantId(tenantId);
    }
}
