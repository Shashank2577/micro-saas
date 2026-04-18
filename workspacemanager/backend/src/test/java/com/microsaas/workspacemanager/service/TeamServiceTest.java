package com.microsaas.workspacemanager.service;

import com.microsaas.workspacemanager.domain.Team;
import com.microsaas.workspacemanager.dto.TeamDto;
import com.microsaas.workspacemanager.repository.TeamRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;
    
    @Mock
    private AuditService auditService;

    @InjectMocks
    private TeamService teamService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateTeam() {
        TeamDto dto = new TeamDto();
        dto.setName("Engineering");
        
        Team team = new Team();
        team.setId(UUID.randomUUID());
        team.setTenantId(tenantId);
        team.setName("Engineering");
        
        when(teamRepository.save(any())).thenReturn(team);
        doNothing().when(auditService).logAction(any(), any(), any(), any(), any());

        Team result = teamService.createTeam(dto);

        assertNotNull(result);
        assertEquals("Engineering", result.getName());
        verify(teamRepository, times(1)).save(any());
        verify(auditService, times(1)).logAction(eq(tenantId), eq("TEAM_CREATED"), isNull(), eq(team.getId()), anyString());
    }

    @Test
    void testGetTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team());
        when(teamRepository.findByTenantId(tenantId)).thenReturn(teams);

        List<Team> result = teamService.getTeams();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(teamRepository, times(1)).findByTenantId(tenantId);
    }
}
