package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.model.*;
import com.microsaas.brandvoice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BrandVoiceService {

    private final BrandProfileRepository brandProfileRepository;
    private final ContentReviewRepository contentReviewRepository;
    private final BrandCorpusItemRepository brandCorpusItemRepository;
    private final ConsistencyTrendRepository consistencyTrendRepository;

    @Transactional
    public BrandProfile createBrandProfile(
            UUID tenantId,
            String name,
            Map<String, Object> toneAttributes,
            List<String> vocabularyApproved,
            List<String> vocabularyBanned,
            String styleGuide
    ) {
        BrandProfile profile = BrandProfile.builder()
                .tenantId(tenantId)
                .name(name)
                .toneAttributes(toneAttributes)
                .vocabularyApproved(vocabularyApproved)
                .vocabularyBanned(vocabularyBanned)
                .styleGuide(styleGuide)
                .consistencyScore(BigDecimal.valueOf(100.0)) // Default initial score
                .build();
        return brandProfileRepository.save(profile);
    }

    public List<BrandProfile> listBrandProfiles(UUID tenantId) {
        return brandProfileRepository.findByTenantId(tenantId);
    }

    public BrandProfile getBrandProfile(UUID id, UUID tenantId) {
        return brandProfileRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Brand profile not found"));
    }

    @Transactional
    public BrandCorpusItem addCorpusItem(UUID brandProfileId, UUID tenantId, String title, String content, boolean approved) {
        BrandProfile profile = getBrandProfile(brandProfileId, tenantId);

        BrandCorpusItem item = BrandCorpusItem.builder()
                .tenantId(tenantId)
                .brandProfileId(profile.getId())
                .title(title)
                .content(content)
                .approved(approved)
                .build();
        return brandCorpusItemRepository.save(item);
    }

    @Transactional
    public ContentReview reviewContent(UUID brandProfileId, UUID tenantId, String content) {
        BrandProfile profile = getBrandProfile(brandProfileId, tenantId);

        // Simulating AI review logic
        BigDecimal score = BigDecimal.valueOf(Math.random() * 20 + 80); // Random score between 80-100
        List<Map<String, Object>> deviations = new ArrayList<>();
        List<Map<String, Object>> suggestions = new ArrayList<>();

        // Add dummy deviation if score is lower
        if (score.compareTo(BigDecimal.valueOf(90)) < 0) {
            Map<String, Object> dev = new HashMap<>();
            dev.put("type", "tone");
            dev.put("description", "Content sounds slightly too informal for the brand.");
            deviations.add(dev);

            Map<String, Object> sug = new HashMap<>();
            sug.put("original", "Hey there guys!");
            sug.put("replacement", "Hello team");
            suggestions.add(sug);
        }

        ContentReview review = ContentReview.builder()
                .tenantId(tenantId)
                .brandProfileId(profile.getId())
                .content(content)
                .consistencyScore(score)
                .deviations(deviations)
                .suggestions(suggestions)
                .build();

        review = contentReviewRepository.save(review);
        updateConsistencyTrend(tenantId, brandProfileId, score);

        return review;
    }

    private void updateConsistencyTrend(UUID tenantId, UUID brandProfileId, BigDecimal latestScore) {
        LocalDate today = LocalDate.now();
        ConsistencyTrend trend = consistencyTrendRepository
                .findByTenantIdAndBrandProfileIdAndPeriod(tenantId, brandProfileId, today)
                .orElse(ConsistencyTrend.builder()
                        .tenantId(tenantId)
                        .brandProfileId(brandProfileId)
                        .period(today)
                        .avgScore(BigDecimal.ZERO)
                        .reviewCount(0)
                        .build());

        int count = trend.getReviewCount();
        BigDecimal currentTotal = trend.getAvgScore().multiply(BigDecimal.valueOf(count));
        BigDecimal newTotal = currentTotal.add(latestScore);

        trend.setReviewCount(count + 1);
        trend.setAvgScore(newTotal.divide(BigDecimal.valueOf(trend.getReviewCount()), 2, java.math.RoundingMode.HALF_UP));

        consistencyTrendRepository.save(trend);
    }

    public List<ConsistencyTrend> getTrends(UUID brandProfileId, UUID tenantId) {
        getBrandProfile(brandProfileId, tenantId); // Validate existence
        return consistencyTrendRepository.findByTenantIdAndBrandProfileIdOrderByPeriodAsc(tenantId, brandProfileId);
    }

    public Map<String, List<String>> getVocabulary(UUID brandProfileId, UUID tenantId) {
        BrandProfile profile = getBrandProfile(brandProfileId, tenantId);
        Map<String, List<String>> vocab = new HashMap<>();
        vocab.put("approved", profile.getVocabularyApproved());
        vocab.put("banned", profile.getVocabularyBanned());
        return vocab;
    }
}
