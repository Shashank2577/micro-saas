package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.Variant;
import com.microsaas.copyoptimizer.repository.VariantRepository;
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
class VariantServiceTest {

    @Mock
    private VariantRepository repository;

    @InjectMocks
    private VariantService service;

    @Test
    void create_ShouldSaveAndReturnEntity() {
        Variant entity = new Variant();
        entity.setName("Test");

        when(repository.save(any(Variant.class))).thenReturn(entity);

        Variant result = service.create(entity);

        assertNotNull(result);
        assertEquals("Test", result.getName());
        verify(repository).save(entity);
    }

    @Test
    void getById_WhenExists_ShouldReturnEntity() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        Variant entity = new Variant();
        entity.setId(id);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(entity));

        Variant result = service.getById(id, tenantId);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }
}
