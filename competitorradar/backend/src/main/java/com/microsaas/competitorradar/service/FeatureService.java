package com.microsaas.competitorradar.service;

import com.microsaas.competitorradar.dto.FeatureDto;
import com.microsaas.competitorradar.dto.CompetitorFeatureDto;
import com.microsaas.competitorradar.model.Feature;
import com.microsaas.competitorradar.model.CompetitorFeature;
import com.microsaas.competitorradar.repository.FeatureRepository;
import com.microsaas.competitorradar.repository.CompetitorFeatureRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;
    private final CompetitorFeatureRepository competitorFeatureRepository;

    public Map<String, Object> getFeatureMatrix() {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        List<FeatureDto> features = featureRepository.findByTenantId(tenantId).stream().map(this::mapFeatureToDto).collect(Collectors.toList());
        List<CompetitorFeatureDto> mappings = competitorFeatureRepository.findByTenantId(tenantId).stream().map(this::mapCompetitorFeatureToDto).collect(Collectors.toList());
        
        Map<String, Object> matrix = new HashMap<>();
        matrix.put("features", features);
        matrix.put("mappings", mappings);
        return matrix;
    }

    private FeatureDto mapFeatureToDto(Feature feature) {
        FeatureDto dto = new FeatureDto();
        dto.setId(feature.getId());
        dto.setName(feature.getName());
        dto.setCategory(feature.getCategory());
        return dto;
    }

    private CompetitorFeatureDto mapCompetitorFeatureToDto(CompetitorFeature cf) {
        CompetitorFeatureDto dto = new CompetitorFeatureDto();
        dto.setId(cf.getId());
        dto.setCompetitorId(cf.getCompetitorId());
        dto.setFeatureId(cf.getFeatureId());
        dto.setStatus(cf.getStatus());
        return dto;
    }
}
