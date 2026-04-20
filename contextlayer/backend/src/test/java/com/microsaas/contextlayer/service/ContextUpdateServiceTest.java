package com.microsaas.contextlayer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerContext;
import com.microsaas.contextlayer.dto.ContextUpdateDTO;
import com.microsaas.contextlayer.repository.ContextVersionRepository;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextUpdateServiceTest {
    @Mock
    private CustomerContextRepository repository;
    @Mock
    private ContextVersionRepository versionRepository;
    @Mock
    private RealtimeSyncService syncService;
    @InjectMocks
    private ContextUpdateService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() { TenantContext.set(tenantId); }

    @AfterEach
    void tearDown() { TenantContext.clear(); }

    @Test
    void testUpdateContext() {
        CustomerContext ctx = new CustomerContext();
        ctx.setCustomerId("c1");
        ctx.setTenantId(tenantId);
        when(repository.findByCustomerIdAndTenantId("c1", tenantId)).thenReturn(Optional.of(ctx));
        when(repository.save(any())).thenReturn(ctx);

        ContextUpdateDTO dto = new ContextUpdateDTO();
        dto.setProfile("{\"name\":\"test\"}");

        CustomerContext result = service.updateContext("c1", dto, "app1");
        assertNotNull(result);
        verify(repository).save(any());
        verify(versionRepository).save(any());
        verify(syncService).publishContextUpdate(any());
    }
}
