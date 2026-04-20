package com.microsaas.contextlayer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerContext;
import com.microsaas.contextlayer.repository.CustomerContextRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextRetrievalServiceTest {
    @Mock
    private CustomerContextRepository repository;
    @InjectMocks
    private ContextRetrievalService service;
    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() { TenantContext.set(tenantId); }

    @AfterEach
    void tearDown() { TenantContext.clear(); }

    @Test
    void testGetContext() {
        CustomerContext ctx = new CustomerContext();
        when(repository.findByCustomerIdAndTenantId("c1", tenantId)).thenReturn(Optional.of(ctx));
        assertTrue(service.getContext("c1").isPresent());
    }
}
