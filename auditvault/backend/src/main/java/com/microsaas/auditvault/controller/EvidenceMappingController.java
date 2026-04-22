package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.model.EvidenceMapping;
import com.microsaas.auditvault.service.EvidenceMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mappings")
@RequiredArgsConstructor
public class EvidenceMappingController {
    private final EvidenceMappingService mappingService;

    @PostMapping("/{id}/approve")
    public EvidenceMapping approveMapping(@PathVariable UUID id) {
        return mappingService.approveMapping(id);
    }

    @PostMapping("/{id}/reject")
    public EvidenceMapping rejectMapping(@PathVariable UUID id) {
        return mappingService.rejectMapping(id);
    }
}
