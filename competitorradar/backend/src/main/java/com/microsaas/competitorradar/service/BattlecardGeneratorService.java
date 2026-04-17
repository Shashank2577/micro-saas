package com.microsaas.competitorradar.service;

import com.microsaas.competitorradar.dto.BattlecardDto;
import com.microsaas.competitorradar.model.Battlecard;
import com.microsaas.competitorradar.repository.BattlecardRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BattlecardGeneratorService {

    private final BattlecardRepository battlecardRepository;

    public BattlecardDto getLatestBattlecard(UUID competitorId) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return battlecardRepository.findTopByCompetitorIdAndTenantIdOrderByGeneratedAtDesc(competitorId, tenantId)
                .map(this::mapToDto)
                .orElse(null);
    }

    public BattlecardDto generateBattlecard(UUID competitorId) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        
        // Simulating LLM battlecard generation
        String generatedContent = "AI Generated Battlecard Content for competitor " + competitorId;
        
        Battlecard battlecard = Battlecard.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .competitorId(competitorId)
                .content(generatedContent)
                .generatedAt(OffsetDateTime.now())
                .build();
                
        battlecard = battlecardRepository.save(battlecard);
        return mapToDto(battlecard);
    }

    private BattlecardDto mapToDto(Battlecard battlecard) {
        BattlecardDto dto = new BattlecardDto();
        dto.setId(battlecard.getId());
        dto.setCompetitorId(battlecard.getCompetitorId());
        dto.setContent(battlecard.getContent());
        dto.setGeneratedAt(battlecard.getGeneratedAt());
        return dto;
    }
}
