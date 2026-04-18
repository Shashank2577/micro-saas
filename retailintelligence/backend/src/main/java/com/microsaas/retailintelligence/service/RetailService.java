package com.microsaas.retailintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.retailintelligence.dto.*;
import com.microsaas.retailintelligence.entity.DemandForecast;
import com.microsaas.retailintelligence.entity.PricingRecommendation;
import com.microsaas.retailintelligence.entity.Sku;
import com.microsaas.retailintelligence.repository.DemandForecastRepository;
import com.microsaas.retailintelligence.repository.PricingRecommendationRepository;
import com.microsaas.retailintelligence.repository.SkuRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RetailService {

    private final SkuRepository skuRepository;
    private final DemandForecastRepository forecastRepository;
    private final PricingRecommendationRepository recommendationRepository;
    private final LiteLLMClient liteLLMClient;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public RetailService(SkuRepository skuRepository,
                         DemandForecastRepository forecastRepository,
                         PricingRecommendationRepository recommendationRepository,
                         LiteLLMClient liteLLMClient,
                         ApplicationEventPublisher eventPublisher,
                         ObjectMapper objectMapper) {
        this.skuRepository = skuRepository;
        this.forecastRepository = forecastRepository;
        this.recommendationRepository = recommendationRepository;
        this.liteLLMClient = liteLLMClient;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    public List<SkuDto> getSkus() {
        return skuRepository.findByTenantId(TenantContext.require())
                .stream().map(this::mapSku).collect(Collectors.toList());
    }

    public SkuDto getSku(UUID id) {
        return skuRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(this::mapSku)
                .orElseThrow(() -> new RuntimeException("SKU not found"));
    }

    @Transactional
    public SkuDto createSku(CreateSkuRequest request) {
        Sku sku = new Sku(
                UUID.randomUUID(),
                TenantContext.require(),
                request.getSkuCode(),
                request.getName(),
                request.getCategory(),
                request.getCostPrice(),
                request.getCurrentPrice(),
                request.getStockQuantity()
        );
        skuRepository.save(sku);
        eventPublisher.publishEvent(new IntegrationEvent("sku.created", sku.getId()));
        return mapSku(sku);
    }

    public List<DemandForecastDto> getForecasts(UUID skuId) {
        return forecastRepository.findByTenantIdAndSkuId(TenantContext.require(), skuId)
                .stream().map(this::mapForecast).collect(Collectors.toList());
    }

    @Transactional
    public void generateForecast(UUID skuId) {
        Sku sku = skuRepository.findByIdAndTenantId(skuId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("SKU not found"));
        
        String prompt = String.format("Analyze historical data and trends for SKU %s. Predict demand for the next 7 days. Return JSON with 'forecasts' array containing { date, predictedDemand, confidenceScore }.", sku.getSkuCode());
        String response = liteLLMClient.generateCompletion(prompt);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode forecasts = root.get("forecasts");
            if (forecasts != null && forecasts.isArray()) {
                for (JsonNode f : forecasts) {
                    DemandForecast forecast = new DemandForecast(
                            UUID.randomUUID(),
                            TenantContext.require(),
                            sku.getId(),
                            LocalDate.parse(f.get("date").asText()),
                            f.get("predictedDemand").asInt(),
                            new BigDecimal(f.get("confidenceScore").asText())
                    );
                    forecastRepository.save(forecast);
                }
            }
            eventPublisher.publishEvent(new IntegrationEvent("forecast.generated", sku.getId()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response", e);
        }
    }

    public List<PricingRecommendationDto> getPendingRecommendations() {
        return recommendationRepository.findByTenantIdAndStatus(TenantContext.require(), "PENDING")
                .stream().map(this::mapRecommendation).collect(Collectors.toList());
    }

    @Transactional
    public void generatePricingRecommendations(UUID skuId) {
        Sku sku = skuRepository.findByIdAndTenantId(skuId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("SKU not found"));
        
        String prompt = String.format("Analyze current stock, cost price %s, and current price %s for SKU %s. Recommend a new price with reasoning, ensuring margin > 20%%. Return JSON with { recommendedPrice, reasoning, marginPercentage }.", sku.getCostPrice(), sku.getCurrentPrice(), sku.getSkuCode());
        String response = liteLLMClient.generateCompletion(prompt);

        try {
            JsonNode node = objectMapper.readTree(response);
            PricingRecommendation recommendation = new PricingRecommendation(
                    UUID.randomUUID(),
                    TenantContext.require(),
                    sku.getId(),
                    new BigDecimal(node.get("recommendedPrice").asText()),
                    node.get("reasoning").asText(),
                    new BigDecimal(node.get("marginPercentage").asText()),
                    "PENDING"
            );
            recommendationRepository.save(recommendation);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response", e);
        }
    }

    @Transactional
    public void applyRecommendation(UUID recommendationId) {
        PricingRecommendation recommendation = recommendationRepository.findByIdAndTenantId(recommendationId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        
        recommendation.setStatus("APPLIED");
        recommendationRepository.save(recommendation);

        Sku sku = skuRepository.findByIdAndTenantId(recommendation.getSkuId(), TenantContext.require())
                .orElseThrow();
        sku.setCurrentPrice(recommendation.getRecommendedPrice());
        skuRepository.save(sku);

        eventPublisher.publishEvent(new IntegrationEvent("sku.price_updated", sku.getId()));
    }

    private SkuDto mapSku(Sku sku) {
        SkuDto dto = new SkuDto();
        dto.setId(sku.getId());
        dto.setSkuCode(sku.getSkuCode());
        dto.setName(sku.getName());
        dto.setCategory(sku.getCategory());
        dto.setCostPrice(sku.getCostPrice());
        dto.setCurrentPrice(sku.getCurrentPrice());
        dto.setStockQuantity(sku.getStockQuantity());
        dto.setCreatedAt(sku.getCreatedAt());
        dto.setUpdatedAt(sku.getUpdatedAt());
        return dto;
    }

    private DemandForecastDto mapForecast(DemandForecast forecast) {
        DemandForecastDto dto = new DemandForecastDto();
        dto.setId(forecast.getId());
        dto.setSkuId(forecast.getSkuId());
        dto.setForecastDate(forecast.getForecastDate());
        dto.setPredictedDemand(forecast.getPredictedDemand());
        dto.setConfidenceScore(forecast.getConfidenceScore());
        return dto;
    }

    private PricingRecommendationDto mapRecommendation(PricingRecommendation rec) {
        PricingRecommendationDto dto = new PricingRecommendationDto();
        dto.setId(rec.getId());
        dto.setSkuId(rec.getSkuId());
        dto.setRecommendedPrice(rec.getRecommendedPrice());
        dto.setReasoning(rec.getReasoning());
        dto.setMarginPercentage(rec.getMarginPercentage());
        dto.setStatus(rec.getStatus());
        return dto;
    }

    public static class IntegrationEvent {
        private final String type;
        private final UUID entityId;
        public IntegrationEvent(String type, UUID entityId) { this.type = type; this.entityId = entityId; }
        public String getType() { return type; }
        public UUID getEntityId() { return entityId; }
    }
}
