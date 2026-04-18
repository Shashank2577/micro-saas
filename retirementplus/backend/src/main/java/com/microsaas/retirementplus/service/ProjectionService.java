package com.microsaas.retirementplus.service;

import com.microsaas.retirementplus.domain.Projection;
import com.microsaas.retirementplus.domain.UserProfile;
import com.microsaas.retirementplus.dto.ProjectionResponseDto;
import com.microsaas.retirementplus.repository.ProjectionRepository;
import com.microsaas.retirementplus.repository.UserProfileRepository;
import com.microsaas.retirementplus.ai.LiteLLMClient;
import com.microsaas.retirementplus.ai.AiProjectionResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import java.util.Map;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.Optional;

@Service
public class ProjectionService {

    private final ProjectionRepository projectionRepository;
    private final UserProfileRepository userProfileRepository;
    private final LiteLLMClient liteLLMClient;
    private final ApplicationEventPublisher eventPublisher;

    public ProjectionService(ProjectionRepository projectionRepository, 
                             UserProfileRepository userProfileRepository,
                             LiteLLMClient liteLLMClient,
                             ApplicationEventPublisher eventPublisher) {
        this.projectionRepository = projectionRepository;
        this.userProfileRepository = userProfileRepository;
        this.liteLLMClient = liteLLMClient;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ProjectionResponseDto generateProjection(UUID userId, UUID tenantId) {
        UserProfile profile = userProfileRepository.findByUserIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Calculate SWR
        BigDecimal swr;
        if (profile.getCurrentSavings().compareTo(BigDecimal.ZERO) == 0) {
            swr = BigDecimal.ZERO;
        } else {
            swr = profile.getDesiredIncome().divide(profile.getCurrentSavings(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }

        BigDecimal probabilityOfSuccess = new BigDecimal("80.0"); // Simplified
        
        // Calculate RMD (simplified estimate if age > 73)
        BigDecimal rmdAmount = BigDecimal.ZERO;
        if (profile.getCurrentAge() >= 73) {
            // Simplified factor
            rmdAmount = profile.getCurrentSavings().divide(new BigDecimal("27.4"), 2, RoundingMode.HALF_UP);
        }

        String prompt = String.format("Generate advanced retirement projections for a %d year old %s planning to retire at %d with %s health, %s family history, aiming to leave %s to heirs. Wants Annuity? %b. Current savings: %s, Desired income: %s.",
                profile.getCurrentAge(), profile.getGender(), profile.getRetirementAge(), profile.getHealthStatus(), profile.getFamilyHistory(), profile.getInheritanceGoal(), profile.getWantsAnnuity(), profile.getCurrentSavings(), profile.getDesiredIncome());

        AiProjectionResult aiResult = liteLLMClient.getProjections(prompt);

        Optional<Projection> existingProjection = projectionRepository.findByUserIdAndTenantId(userId, tenantId);
        Projection projection = existingProjection.orElse(new Projection());

        if (existingProjection.isEmpty()) {
            projection.setId(UUID.randomUUID());
            projection.setTenantId(tenantId);
            projection.setUserId(userId);
            projection.setCreatedAt(ZonedDateTime.now());
        }

        projection.setLifeExpectancy(aiResult.getLifeExpectancy());
        projection.setSafeWithdrawalRate(swr);
        projection.setSocialSecurityClaimingAge(aiResult.getSocialSecurityClaimingAge());
        projection.setEstimatedHealthcareCost(aiResult.getEstimatedHealthcareCost());
        projection.setQcdOpportunityAge(aiResult.getQcdOpportunityAge());
        projection.setProbabilityOfSuccess(probabilityOfSuccess);
        
        projection.setRothConversionAmount(aiResult.getRothConversionAmount());
        projection.setRmdAmount(rmdAmount);
        projection.setStressTestSurvivalRate(aiResult.getStressTestSurvivalRate());
        projection.setAnnuityGuaranteedIncome(aiResult.getAnnuityGuaranteedIncome());
        projection.setTaxStrategyOrder(aiResult.getTaxStrategyOrder());
        
        projection.setUpdatedAt(ZonedDateTime.now());

        Projection saved = projectionRepository.save(projection);
        
        eventPublisher.publishEvent(Map.of(
            "eventType", "retirementplus.projection_generated",
            "tenantId", tenantId,
            "userId", userId,
            "projectionId", saved.getId()
        ));

        ProjectionResponseDto response = new ProjectionResponseDto();
        response.setLifeExpectancy(projection.getLifeExpectancy());
        response.setSafeWithdrawalRate(projection.getSafeWithdrawalRate());
        response.setSocialSecurityClaimingAge(projection.getSocialSecurityClaimingAge());
        response.setEstimatedHealthcareCost(projection.getEstimatedHealthcareCost());
        response.setQcdOpportunityAge(projection.getQcdOpportunityAge());
        response.setProbabilityOfSuccess(projection.getProbabilityOfSuccess());
        
        response.setRothConversionAmount(projection.getRothConversionAmount());
        response.setRmdAmount(projection.getRmdAmount());
        response.setStressTestSurvivalRate(projection.getStressTestSurvivalRate());
        response.setAnnuityGuaranteedIncome(projection.getAnnuityGuaranteedIncome());
        response.setTaxStrategyOrder(projection.getTaxStrategyOrder());

        return response;
    }
}
