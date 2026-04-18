package com.microsaas.workspacemanager.service;

import com.microsaas.workspacemanager.domain.Team;
import com.microsaas.workspacemanager.dto.TeamDto;
import com.microsaas.workspacemanager.repository.TeamRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final AuditService auditService;

    public List<Team> getTeams() {
        return teamRepository.findByTenantId(TenantContext.require());
    }

    public Team createTeam(TeamDto dto) {
        UUID tenantId = TenantContext.require();
        Team team = new Team();
        team.setTenantId(tenantId);
        team.setName(dto.getName());
        team.setParentTeamId(dto.getParentTeamId());
        team = teamRepository.save(team);

        auditService.logAction(tenantId, "TEAM_CREATED", null, team.getId(), "{\"name\":\"" + dto.getName() + "\"}");
        return team;
    }

    public Team updateTeam(UUID teamId, TeamDto dto) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        team.setName(dto.getName());
        team.setParentTeamId(dto.getParentTeamId());
        team = teamRepository.save(team);

        auditService.logAction(team.getTenantId(), "TEAM_UPDATED", null, team.getId(), "{}");
        return team;
    }

    public void deleteTeam(UUID teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        teamRepository.delete(team);

        auditService.logAction(team.getTenantId(), "TEAM_DELETED", null, teamId, "{}");
    }
}
