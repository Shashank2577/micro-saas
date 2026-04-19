package com.microsaas.cashflowai.service;

import com.microsaas.cashflowai.model.CashPosition;
import com.microsaas.cashflowai.repository.CashPositionRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CashPositionServiceTest {

    @Mock
    private CashPositionRepository repository;

    @InjectMocks
    private CashPositionService service;

    @Test
    void testGetById() {
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        CashPosition cp = new CashPosition();
        cp.setId(id);
        cp.setTenantId(tenantId);

        try (MockedStatic<TenantContext> mocked = Mockito.mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);
            when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(cp));

            CashPosition result = service.getById(id);
            assertNotNull(result);
        }
    }
}
