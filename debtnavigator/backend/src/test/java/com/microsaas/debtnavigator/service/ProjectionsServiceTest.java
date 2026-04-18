package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.RiskProjection;
import com.microsaas.debtnavigator.repository.RiskProjectionRepository;
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
class ProjectionsServiceTest {

    @Mock
    private RiskProjectionRepository repository;

    @InjectMocks
    private ProjectionsService service;

    @Test
    void testCreate() {
        RiskProjection projection = new RiskProjection();
        when(repository.save(any(RiskProjection.class))).thenReturn(projection);
        assertNotNull(service.create(new RiskProjection()));
        verify(repository).save(any(RiskProjection.class));
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new RiskProjection()));
        assertTrue(service.getById(id, tenantId).isPresent());
    }
}
