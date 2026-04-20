package com.microsaas.contextlayer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextEnrichmentServiceTest {
    @Mock
    private ContextRetrievalService retrievalService;
    @Mock
    private ContextUpdateService updateService;
    @InjectMocks
    private ContextEnrichmentService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() { TenantContext.set(tenantId); }

    @AfterEach
    void tearDown() { TenantContext.clear(); }

    @Test
    void testEnrichContext() {
        CustomerContext ctx = new CustomerContext();
        ctx.setAttributes("{}");
        when(retrievalService.getContext("c1")).thenReturn(Optional.of(ctx));

        service.enrichContext("c1");

        verify(updateService).updateContext(eq("c1"), any(), eq("EnrichmentService"));
    }
}
