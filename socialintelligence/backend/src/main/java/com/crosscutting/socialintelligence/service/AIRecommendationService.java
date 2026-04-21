package com.crosscutting.socialintelligence.service;

import com.crosscutting.socialintelligence.domain.GrowthRecommendation;
import com.crosscutting.socialintelligence.repository.GrowthRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AIRecommendationService {
    private final GrowthRecommendationRepository repository;

    public List<GrowthRecommendation> generateRecommendations(UUID tenantId) {
        GrowthRecommendation rec = GrowthRecommendation.builder()
                .tenantId(tenantId)
                .recommendationText("Post more videos")
                .platform("ALL")
                .category("CONTENT")
                .priority(1)
                .isActioned(false)
                .build();
        return List.of(repository.save(rec));
    }

    public GrowthRecommendation markActioned(UUID id) {
        GrowthRecommendation rec = repository.findById(id).orElseThrow();
        rec.setIsActioned(true);
        return repository.save(rec);
    }

    public List<GrowthRecommendation> getRecommendations(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
