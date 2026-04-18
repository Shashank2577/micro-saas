package com.microsaas.churnpredictor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.churnpredictor.dto.InterventionPlaybookDto;
import com.microsaas.churnpredictor.entity.InterventionPlaybook;
import com.microsaas.churnpredictor.repository.CustomerHealthScoreRepository;
import com.microsaas.churnpredictor.repository.CustomerRepository;
import com.microsaas.churnpredictor.repository.InterventionPlaybookRepository;
import com.microsaas.churnpredictor.repository.InterventionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InterventionServiceTest {
    @Mock
    private InterventionRepository interventionRepository;

    @Mock
    private InterventionPlaybookRepository playbookRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerHealthScoreRepository healthScoreRepository;

    @Mock
    private AiOfferService aiOfferService;

    @InjectMocks
    private InterventionService interventionService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @Test
    void createPlaybook() {
        InterventionPlaybookDto input = new InterventionPlaybookDto();
        input.setName("Test Playbook");

        InterventionPlaybook saved = new InterventionPlaybook();
        saved.setId(UUID.randomUUID());
        saved.setName("Test Playbook");

        when(playbookRepository.save(any())).thenReturn(saved);

        InterventionPlaybookDto result = interventionService.createPlaybook(input);
        assertEquals("Test Playbook", result.getName());
    }
}
