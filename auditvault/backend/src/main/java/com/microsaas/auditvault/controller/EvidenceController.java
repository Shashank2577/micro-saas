package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.dto.EvidenceIngestRequest;
import com.microsaas.auditvault.model.Evidence;
import com.microsaas.auditvault.model.EvidenceMapping;
import com.microsaas.auditvault.service.AiMappingService;
import com.microsaas.auditvault.service.EvidenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/evidence")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;
    private final AiMappingService aiMappingService;

    @PostMapping
    public Evidence ingestEvidence(@RequestBody EvidenceIngestRequest request) {
        return evidenceService.ingestEvidence(request);
    }

    @GetMapping
    public List<Evidence> listEvidence() {
        return evidenceService.listEvidence();
    }

    @GetMapping("/{id}")
    public Evidence getEvidence(@PathVariable UUID id) {
        return evidenceService.getEvidence(id);
    }

    @PostMapping("/{id}/map")
    public EvidenceMapping mapEvidence(@PathVariable UUID id, @RequestParam UUID frameworkId) {
        return aiMappingService.mapEvidence(id, frameworkId);
    }
}
