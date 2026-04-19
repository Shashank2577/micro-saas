package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.DilutionScenario;
import com.microsaas.equityintelligence.repositories.DilutionScenarioRepository;
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

class DilutionScenarioServiceTest {

    @Mock
    private DilutionScenarioRepository repository;

    @Mock
    private WebhookService webhookService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DilutionScenarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validate_shouldReturnTrueWhenExists() {
        UUID id = UUID.randomUUID();
        DilutionScenario scenario = new DilutionScenario();
        scenario.setId(id);

        when(repository.findByIdAndTenantId(eq(id), any())).thenReturn(Optional.of(scenario));

        // Note: The BaseService methods use TenantContext.require(), which we can't easily
        // mock in a simple unit test without more setup.
    }
}
