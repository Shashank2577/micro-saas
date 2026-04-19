package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.FundingRound;
import com.microsaas.equityintelligence.repositories.FundingRoundRepository;
import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FundingRoundServiceTest {

    @Mock
    private FundingRoundRepository repository;

    @Mock
    private WebhookService webhookService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FundingRoundService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void simulate_shouldReturnSuccessString() {
        UUID id = UUID.randomUUID();
        String result = service.simulate(id);
        assertTrue(result.contains("simulation completed for"));
    }
}
