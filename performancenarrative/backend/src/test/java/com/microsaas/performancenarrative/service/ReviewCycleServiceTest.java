package com.microsaas.performancenarrative.service;

import com.microsaas.performancenarrative.entity.ReviewCycle;
import com.microsaas.performancenarrative.repository.ReviewCycleRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ReviewCycleServiceTest {

    private ReviewCycleRepository repository;
    private ReviewCycleService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ReviewCycleRepository.class);
        service = new ReviewCycleService(repository);
        TenantContext.set(UUID.randomUUID());
    }

    @Test
    void testCreate() {
        ReviewCycle input = new ReviewCycle();
        input.setName("Q1 Review");

        Mockito.when(repository.save(any(ReviewCycle.class))).thenAnswer(i -> i.getArgument(0));

        ReviewCycle result = service.create(input);

        assertNotNull(result.getId());
        assertEquals("Q1 Review", result.getName());
        assertEquals("DRAFT", result.getStatus());
    }
}
