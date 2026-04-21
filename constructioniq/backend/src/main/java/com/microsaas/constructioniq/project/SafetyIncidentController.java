package com.microsaas.constructioniq.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/incidents")
@RequiredArgsConstructor
public class SafetyIncidentController {

    private final SafetyIncidentService incidentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SafetyIncident reportIncident(@PathVariable UUID projectId, @RequestBody SafetyIncident incident) {
        return incidentService.reportIncident(projectId, incident);
    }

    @GetMapping
    public List<SafetyIncident> getIncidents(@PathVariable UUID projectId) {
        return incidentService.getIncidents(projectId);
    }

    @GetMapping("/{id}")
    public SafetyIncident getIncident(@PathVariable UUID id) {
        return incidentService.getIncident(id);
    }

    @PutMapping("/{id}")
    public SafetyIncident updateIncident(@PathVariable UUID id, @RequestBody SafetyIncident incident) {
        return incidentService.updateIncident(id, incident);
    }
}
