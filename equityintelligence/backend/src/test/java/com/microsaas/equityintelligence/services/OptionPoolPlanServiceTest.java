package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.OptionPoolPlan;
import com.microsaas.equityintelligence.repositories.OptionPoolPlanRepository;
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

class OptionPoolPlanServiceTest {

    @Mock
    private OptionPoolPlanRepository repository;

    @InjectMocks
    private OptionPoolPlanService service;

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
