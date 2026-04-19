package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.WinningVariant;
import com.microsaas.copyoptimizer.repository.WinningVariantRepository;
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
class WinningVariantServiceTest {

    @Mock
    private WinningVariantRepository repository;

    @InjectMocks
    private WinningVariantService service;

    @Test
    void create_ShouldSaveAndReturnEntity() {
        WinningVariant entity = new WinningVariant();
        entity.setName("Test WinningVariant");

        when(repository.save(any(WinningVariant.class))).thenReturn(entity);

        WinningVariant result = service.create(entity);

        assertNotNull(result);
        assertEquals("Test WinningVariant", result.getName());
    }

    @Test
    void getById_WhenExists_ShouldReturnEntity() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        WinningVariant entity = new WinningVariant();
        entity.setId(id);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(entity));

        WinningVariant result = service.getById(id, tenantId);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }
}
