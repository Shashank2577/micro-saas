package com.microsaas.competitorradar.service;

import com.microsaas.competitorradar.dto.AlertDto;
import com.microsaas.competitorradar.model.Alert;
import com.microsaas.competitorradar.repository.AlertRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public List<AlertDto> getAlerts() {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return alertRepository.findByTenantIdOrderByCreatedAtDesc(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AlertDto mapToDto(Alert alert) {
        AlertDto dto = new AlertDto();
        dto.setId(alert.getId());
        dto.setType(alert.getType());
        dto.setSeverity(alert.getSeverity());
        dto.setMessage(alert.getMessage());
        dto.setCompetitorId(alert.getCompetitorId());
        dto.setCreatedAt(alert.getCreatedAt());
        return dto;
    }
}
