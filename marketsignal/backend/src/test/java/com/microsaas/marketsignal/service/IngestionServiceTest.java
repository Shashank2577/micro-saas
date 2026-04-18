package com.microsaas.marketsignal.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.marketsignal.domain.entity.MarketSignal;
import com.microsaas.marketsignal.dto.IngestSignalRequest;
import com.microsaas.marketsignal.repository.InformationSourceRepository;
import com.microsaas.marketsignal.repository.MarketSignalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestionServiceTest {

    @Mock
    private MarketSignalRepository signalRepository;

    @Mock
    private InformationSourceRepository sourceRepository;

    @Mock
    private LiteLLMClient liteLLMClient;

    @InjectMocks
    private IngestionService ingestionService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testIngestSignal() {
        IngestSignalRequest request = new IngestSignalRequest();
        request.setTitle("New Tech Release");
        request.setContent("A major company released a new AI product.");
        
        when(liteLLMClient.generateEmbedding(anyString())).thenReturn(new float[]{0.1f, 0.2f});
        when(liteLLMClient.generateCompletion(anyString(), anyString())).thenReturn("Strength: 8\nSentiment: Positive\nImplications: Good.");
        
        when(signalRepository.save(any(MarketSignal.class))).thenAnswer(invocation -> {
            MarketSignal signal = invocation.getArgument(0);
            signal.setId(UUID.randomUUID());
            return signal;
        });

        MarketSignal signal = ingestionService.ingestSignal(request);

        assertNotNull(signal.getId());
        assertEquals("New Tech Release", signal.getTitle());
        assertEquals(8, signal.getSignalStrength());
        assertEquals("Positive", signal.getSentiment());
        verify(signalRepository, times(1)).save(any());
    }
}
