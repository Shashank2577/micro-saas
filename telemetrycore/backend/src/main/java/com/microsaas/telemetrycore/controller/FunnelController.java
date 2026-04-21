package com.microsaas.telemetrycore.controller;

import com.microsaas.telemetrycore.dto.FunnelDTO;
import com.microsaas.telemetrycore.dto.FunnelAnalysisDTO;
import com.microsaas.telemetrycore.model.Funnel;
import com.microsaas.telemetrycore.service.FunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funnels")
public class FunnelController {

    private final FunnelService funnelService;

    @Autowired
    public FunnelController(FunnelService funnelService) {
        this.funnelService = funnelService;
    }

    @PostMapping
    public ResponseEntity<FunnelDTO> createFunnel(@RequestBody FunnelDTO funnelDTO) {
        Funnel funnel = new Funnel();
        funnel.setName(funnelDTO.getName());
        funnel.setSteps(funnelDTO.getSteps());

        Funnel created = funnelService.createFunnel(funnel);
        return ResponseEntity.ok(mapToDTO(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunnelDTO> getFunnel(@PathVariable UUID id) {
        Funnel funnel = funnelService.getFunnel(id);
        return ResponseEntity.ok(mapToDTO(funnel));
    }

    @GetMapping("/{id}/analysis")
    public ResponseEntity<FunnelAnalysisDTO> analyzeFunnel(@PathVariable UUID id) {
        Map<String, Object> analysis = funnelService.analyzeFunnel(id);
        return ResponseEntity.ok(new FunnelAnalysisDTO(id, analysis));
    }

    @GetMapping
    public ResponseEntity<List<FunnelDTO>> getAllFunnels() {
        List<Funnel> funnels = funnelService.getAllFunnels();
        List<FunnelDTO> dtos = funnels.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFunnel(@PathVariable UUID id) {
        funnelService.deleteFunnel(id);
        return ResponseEntity.noContent().build();
    }

    private FunnelDTO mapToDTO(Funnel funnel) {
        FunnelDTO dto = new FunnelDTO();
        dto.setId(funnel.getId());
        dto.setName(funnel.getName());
        dto.setSteps(funnel.getSteps());
        dto.setCreatedAt(funnel.getCreatedAt());
        dto.setUpdatedAt(funnel.getUpdatedAt());
        return dto;
    }
}
