package com.microsaas.churnpredictor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.churnpredictor.dto.CustomerHealthScoreDto;
import com.microsaas.churnpredictor.entity.Customer;
import com.microsaas.churnpredictor.entity.CustomerHealthScore;
import com.microsaas.churnpredictor.repository.CustomerHealthScoreRepository;
import com.microsaas.churnpredictor.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HealthScoreServiceTest {
    @Mock
    private CustomerHealthScoreRepository healthScoreRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private HealthScoreService healthScoreService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @Test
    void recalculateHealthScore() {
        Customer c = new Customer();
        c.setId(customerId);
        c.setTenantId(tenantId);
        
        when(customerRepository.findByIdAndTenantId(customerId, tenantId)).thenReturn(Optional.of(c));

        CustomerHealthScore mockScore = new CustomerHealthScore();
        mockScore.setCustomer(c);
        mockScore.setCompositeScore(BigDecimal.valueOf(80));

        when(healthScoreRepository.save(any())).thenReturn(mockScore);

        CustomerHealthScoreDto result = healthScoreService.recalculateHealthScore(customerId);
        assertNotNull(result);
    }
}
