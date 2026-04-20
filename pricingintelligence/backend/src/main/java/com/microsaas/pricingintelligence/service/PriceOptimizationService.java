package com.microsaas.pricingintelligence.service;

import com.microsaas.pricingintelligence.domain.ElasticityModel;
import com.microsaas.pricingintelligence.domain.PriceRecommendation;
import com.microsaas.pricingintelligence.repository.ElasticityModelRepository;
import com.microsaas.pricingintelligence.repository.PriceRecommendationRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceOptimizationService {

    private final PriceRecommendationRepository priceRecommendationRepository;
    private final ElasticityModelRepository elasticityModelRepository;
    private final AiService aiService;

    public List<PriceRecommendation> getRecommendations() {
        return priceRecommendationRepository.findByTenantId(TenantContext.require());
    }

    @Transactional
    public PriceRecommendation generateRecommendation(UUID segmentId) {
        UUID tenantId = TenantContext.require();

        Optional<ElasticityModel> modelOpt = elasticityModelRepository.findByTenantIdAndSegmentId(tenantId, segmentId);
        if (modelOpt.isEmpty()) {
            throw new IllegalArgumentException("No elasticity model found for segment");
        }

        ElasticityModel model = modelOpt.get();

        PriceRecommendation rec = new PriceRecommendation();
        rec.setTenantId(tenantId);
        rec.setSegmentId(segmentId);
        rec.setCurrentPrice(new BigDecimal("50.00")); // Hardcoded for simulation

        // Optimize based on simulated elasticity
        double recommended = 50.00 * (1 + (model.getElasticityCoefficient() * 0.1));
        rec.setRecommendedPrice(BigDecimal.valueOf(recommended));
        rec.setConfidenceScore(model.getRSquared());
        rec.setEstimatedRevenueLift(15.5);

        String prompt = String.format("Based on the elasticity coefficient of %.2f for the segment %s, currently priced at %.2f, write a brief rationale for changing the price to %.2f aiming to maximize revenue while minimizing churn.",
                model.getElasticityCoefficient(), segmentId.toString(), rec.getCurrentPrice().doubleValue(), rec.getRecommendedPrice().doubleValue());

        ChatRequest request = new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), null, null);
        String rationale = aiService.chat(request).content();

        rec.setRationale(rationale);

        return priceRecommendationRepository.save(rec);
    }
}
