package com.microsaas.incidentbrain.api;

import com.microsaas.incidentbrain.domain.model.Incident;
import com.microsaas.incidentbrain.domain.model.RootCauseCandidate;
import com.microsaas.incidentbrain.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.createIncident(incident));
    }

    @GetMapping
    public ResponseEntity<List<Incident>> listIncidents() {
        return ResponseEntity.ok(incidentService.listIncidents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncident(@PathVariable String id) {
        return incidentService.getIncident(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/analyze")
    public ResponseEntity<RootCauseCandidate> analyzeIncident(@PathVariable String id) {
        return ResponseEntity.ok(incidentService.analyzeIncident(id));
    }

    @PostMapping("/{id}/postmortem")
    public ResponseEntity<String> generatePostmortem(@PathVariable String id) {
        return ResponseEntity.ok(incidentService.generatePostmortem(id));
    }

    @PostMapping("/{id}/datadog/logs")
    public ResponseEntity<Void> ingestDatadogLogs(@PathVariable String id, @RequestParam String service) {
        incidentService.ingestDatadogLogs(id, service);
        return ResponseEntity.ok().build();
    }
}
