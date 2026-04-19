package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.RenewalAlert;
import com.microsaas.contractportfolio.repository.RenewalAlertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenewalAlertServiceTest {

    @Mock
    private RenewalAlertRepository repository;

    @InjectMocks
    private RenewalAlertService service;

    @Test
    void testCreate() {
        RenewalAlert entity = new RenewalAlert();
        when(repository.save(any(RenewalAlert.class))).thenReturn(entity);
        RenewalAlert result = service.create(entity);
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new RenewalAlert()));
        Optional<RenewalAlert> result = service.getById(id, tenantId);
        assertTrue(result.isPresent());
    }
}
