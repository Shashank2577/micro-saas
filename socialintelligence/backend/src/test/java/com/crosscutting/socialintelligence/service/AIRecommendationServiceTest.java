package com.crosscutting.socialintelligence.service;

import com.crosscutting.socialintelligence.domain.GrowthRecommendation;
import com.crosscutting.socialintelligence.repository.GrowthRecommendationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AIRecommendationServiceTest {

    @Mock
    private GrowthRecommendationRepository repository;

    @InjectMocks
    private AIRecommendationService service;

    @Test
    void testGenerateRecommendations() {
        UUID tenantId = UUID.randomUUID();
        GrowthRecommendation mockRec = GrowthRecommendation.builder()
                .tenantId(tenantId)
                .recommendationText("Post more videos")
                .build();
        when(repository.save(any(GrowthRecommendation.class))).thenReturn(mockRec);

        List<GrowthRecommendation> recommendations = service.generateRecommendations(tenantId);

        assertEquals(1, recommendations.size());
        assertEquals("Post more videos", recommendations.get(0).getRecommendationText());
    }
}
