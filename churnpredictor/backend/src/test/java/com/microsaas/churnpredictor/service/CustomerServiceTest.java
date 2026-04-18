package com.microsaas.churnpredictor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.churnpredictor.dto.CustomerDto;
import com.microsaas.churnpredictor.entity.Customer;
import com.microsaas.churnpredictor.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private HealthScoreService healthScoreService;

    @Mock
    private PredictionService predictionService;

    @InjectMocks
    private CustomerService customerService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @Test
    void getAllCustomers() {
        Customer c = new Customer();
        c.setId(customerId);
        c.setTenantId(tenantId);
        c.setName("Test Corp");
        
        when(customerRepository.findByTenantId(tenantId)).thenReturn(List.of(c));

        List<CustomerDto> result = customerService.getAllCustomers();
        assertEquals(1, result.size());
        assertEquals("Test Corp", result.get(0).getName());
    }

    @Test
    void createCustomer() {
        CustomerDto input = new CustomerDto();
        input.setName("New Corp");
        input.setIndustry("Tech");
        input.setMrr(BigDecimal.valueOf(1000));

        Customer saved = new Customer();
        saved.setId(customerId);
        saved.setTenantId(tenantId);
        saved.setName("New Corp");

        when(customerRepository.save(any())).thenReturn(saved);

        CustomerDto result = customerService.createCustomer(input);
        assertEquals("New Corp", result.getName());
        assertEquals(customerId, result.getId());
    }
}
