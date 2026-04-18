package com.microsaas.retailintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.retailintelligence.dto.CreateSkuRequest;
import com.microsaas.retailintelligence.dto.SkuDto;
import com.microsaas.retailintelligence.entity.Sku;
import com.microsaas.retailintelligence.repository.DemandForecastRepository;
import com.microsaas.retailintelligence.repository.PricingRecommendationRepository;
import com.microsaas.retailintelligence.repository.SkuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RetailServiceTest {

    private SkuRepository skuRepository;
    private DemandForecastRepository forecastRepository;
    private PricingRecommendationRepository recommendationRepository;
    private LiteLLMClient liteLLMClient;
    private ApplicationEventPublisher eventPublisher;
    private ObjectMapper objectMapper;
    private RetailService retailService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        skuRepository = mock(SkuRepository.class);
        forecastRepository = mock(DemandForecastRepository.class);
        recommendationRepository = mock(PricingRecommendationRepository.class);
        liteLLMClient = mock(LiteLLMClient.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        objectMapper = new ObjectMapper();

        retailService = new RetailService(skuRepository, forecastRepository, recommendationRepository, liteLLMClient, eventPublisher, objectMapper);
        TenantContext.set(tenantId);
    }

    @Test
    void createSku_success() {
        CreateSkuRequest req = new CreateSkuRequest();
        req.setSkuCode("SKU123");
        req.setName("Test Product");
        req.setCostPrice(new BigDecimal("10.00"));
        req.setCurrentPrice(new BigDecimal("15.00"));
        req.setStockQuantity(100);

        SkuDto result = retailService.createSku(req);

        assertNotNull(result);
        assertEquals("SKU123", result.getSkuCode());

        ArgumentCaptor<Sku> captor = ArgumentCaptor.forClass(Sku.class);
        verify(skuRepository).save(captor.capture());
        assertEquals(tenantId, captor.getValue().getTenantId());

        verify(eventPublisher).publishEvent(any(RetailService.IntegrationEvent.class));
    }

    @Test
    void getSku_found() {
        UUID skuId = UUID.randomUUID();
        Sku sku = new Sku();
        sku.setId(skuId);
        sku.setTenantId(tenantId);
        sku.setSkuCode("SKU123");

        when(skuRepository.findByIdAndTenantId(skuId, tenantId)).thenReturn(Optional.of(sku));

        SkuDto result = retailService.getSku(skuId);
        assertNotNull(result);
        assertEquals("SKU123", result.getSkuCode());
    }
}
