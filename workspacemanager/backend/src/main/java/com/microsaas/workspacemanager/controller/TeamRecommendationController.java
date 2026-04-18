package com.microsaas.workspacemanager.controller;

import com.microsaas.workspacemanager.service.LiteLLMClient;
import com.microsaas.workspacemanager.service.TeamService;
import com.microsaas.workspacemanager.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/teams/recommendations")
@RequiredArgsConstructor
public class TeamRecommendationController {

    private final LiteLLMClient liteLLMClient;
    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<String> getRecommendations() {
        List<Team> teams = teamService.getTeams();
        String structure = teams.stream().map(Team::getName).collect(Collectors.joining(", "));
        return ResponseEntity.ok(liteLLMClient.getTeamRecommendation(structure));
    }
}
