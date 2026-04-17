package com.microsaas.competitorradar.service;

import com.microsaas.competitorradar.dto.*;
import com.microsaas.competitorradar.model.*;
import com.microsaas.competitorradar.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final ProductChangeRepository productChangeRepository;
    private final PricingChangeRepository pricingChangeRepository;
    private final HiringSignalRepository hiringSignalRepository;
    private final CustomerReviewRepository customerReviewRepository;
    private final SocialMentionRepository socialMentionRepository;
    private final PressMentionRepository pressMentionRepository;

    public List<ProductChangeDto> getProductChanges(UUID competitorId) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return productChangeRepository.findByCompetitorIdAndTenantIdOrderByDetectedAtDesc(competitorId, tenantId).stream()
                .map(this::mapProductChangeToDto)
                .collect(Collectors.toList());
    }

    public List<PricingChangeDto> getPricingChanges(UUID competitorId) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return pricingChangeRepository.findByCompetitorIdAndTenantIdOrderByDetectedAtDesc(competitorId, tenantId).stream()
                .map(this::mapPricingChangeToDto)
                .collect(Collectors.toList());
    }

    public List<HiringSignalDto> getHiringSignals(UUID competitorId) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return hiringSignalRepository.findByCompetitorIdAndTenantIdOrderByPostedAtDesc(competitorId, tenantId).stream()
                .map(this::mapHiringSignalToDto)
                .collect(Collectors.toList());
    }

    public List<CustomerReviewDto> getReviews(UUID competitorId) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return customerReviewRepository.findByCompetitorIdAndTenantIdOrderByPostedAtDesc(competitorId, tenantId).stream()
                .map(this::mapCustomerReviewToDto)
                .collect(Collectors.toList());
    }

    public List<SocialMentionDto> getSocialMentions(UUID competitorId) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return socialMentionRepository.findByCompetitorIdAndTenantIdOrderByPostedAtDesc(competitorId, tenantId).stream()
                .map(this::mapSocialMentionToDto)
                .collect(Collectors.toList());
    }

    public List<PressMentionDto> getPressMentions(UUID competitorId) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return pressMentionRepository.findByCompetitorIdAndTenantIdOrderByPublishedAtDesc(competitorId, tenantId).stream()
                .map(this::mapPressMentionToDto)
                .collect(Collectors.toList());
    }

    private ProductChangeDto mapProductChangeToDto(ProductChange change) {
        ProductChangeDto dto = new ProductChangeDto();
        dto.setId(change.getId());
        dto.setCompetitorId(change.getCompetitorId());
        dto.setTitle(change.getTitle());
        dto.setDescription(change.getDescription());
        dto.setUrl(change.getUrl());
        dto.setDetectedAt(change.getDetectedAt());
        dto.setStatus(change.getStatus());
        return dto;
    }

    private PricingChangeDto mapPricingChangeToDto(PricingChange change) {
        PricingChangeDto dto = new PricingChangeDto();
        dto.setId(change.getId());
        dto.setCompetitorId(change.getCompetitorId());
        dto.setOldPrice(change.getOldPrice());
        dto.setNewPrice(change.getNewPrice());
        dto.setPlanName(change.getPlanName());
        dto.setDetectedAt(change.getDetectedAt());
        return dto;
    }

    private HiringSignalDto mapHiringSignalToDto(HiringSignal signal) {
        HiringSignalDto dto = new HiringSignalDto();
        dto.setId(signal.getId());
        dto.setCompetitorId(signal.getCompetitorId());
        dto.setRoleTitle(signal.getRoleTitle());
        dto.setDepartment(signal.getDepartment());
        dto.setLocation(signal.getLocation());
        dto.setSource(signal.getSource());
        dto.setPostedAt(signal.getPostedAt());
        return dto;
    }

    private CustomerReviewDto mapCustomerReviewToDto(CustomerReview review) {
        CustomerReviewDto dto = new CustomerReviewDto();
        dto.setId(review.getId());
        dto.setCompetitorId(review.getCompetitorId());
        dto.setPlatform(review.getPlatform());
        dto.setRating(review.getRating());
        dto.setText(review.getText());
        dto.setCategory(review.getCategory());
        dto.setSentimentScore(review.getSentimentScore());
        dto.setPostedAt(review.getPostedAt());
        return dto;
    }

    private SocialMentionDto mapSocialMentionToDto(SocialMention mention) {
        SocialMentionDto dto = new SocialMentionDto();
        dto.setId(mention.getId());
        dto.setCompetitorId(mention.getCompetitorId());
        dto.setPlatform(mention.getPlatform());
        dto.setText(mention.getText());
        dto.setUrl(mention.getUrl());
        dto.setSentimentScore(mention.getSentimentScore());
        dto.setPostedAt(mention.getPostedAt());
        return dto;
    }

    private PressMentionDto mapPressMentionToDto(PressMention mention) {
        PressMentionDto dto = new PressMentionDto();
        dto.setId(mention.getId());
        dto.setCompetitorId(mention.getCompetitorId());
        dto.setSource(mention.getSource());
        dto.setTitle(mention.getTitle());
        dto.setUrl(mention.getUrl());
        dto.setSentimentScore(mention.getSentimentScore());
        dto.setPublishedAt(mention.getPublishedAt());
        return dto;
    }
}
