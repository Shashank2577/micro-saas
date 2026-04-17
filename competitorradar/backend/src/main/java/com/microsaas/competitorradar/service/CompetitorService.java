package com.microsaas.competitorradar.service;

import com.microsaas.competitorradar.dto.CompetitorDto;
import com.microsaas.competitorradar.model.Competitor;
import com.microsaas.competitorradar.repository.CompetitorRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitorService {

    private final CompetitorRepository competitorRepository;

    public List<CompetitorDto> getCompetitors() {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return competitorRepository.findByTenantId(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CompetitorDto addCompetitor(CompetitorDto dto) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        Competitor competitor = Competitor.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .name(dto.getName())
                .website(dto.getWebsite())
                .industry(dto.getIndustry())
                .description(dto.getDescription())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        competitor = competitorRepository.save(competitor);
        return mapToDto(competitor);
    }

    public void removeCompetitor(UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        competitorRepository.findByIdAndTenantId(id, tenantId).ifPresent(competitorRepository::delete);
    }

    private CompetitorDto mapToDto(Competitor competitor) {
        CompetitorDto dto = new CompetitorDto();
        dto.setId(competitor.getId());
        dto.setName(competitor.getName());
        dto.setWebsite(competitor.getWebsite());
        dto.setIndustry(competitor.getIndustry());
        dto.setDescription(competitor.getDescription());
        dto.setCreatedAt(competitor.getCreatedAt());
        dto.setUpdatedAt(competitor.getUpdatedAt());
        return dto;
    }
}
