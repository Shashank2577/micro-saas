package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.BuddyPairRequest;
import com.microsaas.onboardflow.model.BuddyPair;
import com.microsaas.onboardflow.repository.BuddyPairRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuddyPairServiceTest {

    @Mock
    private BuddyPairRepository repository;

    @InjectMocks
    private BuddyPairService service;

    @Test
    public void testFindAll() {
        UUID tenantId = UUID.randomUUID();
        when(repository.findByTenantId(tenantId)).thenReturn(Collections.emptyList());

        assertTrue(service.findAll(tenantId).isEmpty());
        verify(repository, times(1)).findByTenantId(tenantId);
    }

    @Test
    public void testCreate() {
        UUID tenantId = UUID.randomUUID();
        BuddyPairRequest request = new BuddyPairRequest();
        request.setName("Test");

        BuddyPair savedEntity = new BuddyPair();
        savedEntity.setName("Test");

        when(repository.save(any(BuddyPair.class))).thenReturn(savedEntity);

        BuddyPair result = service.create(tenantId, request);
        assertEquals("Test", result.getName());
    }

    @Test
    public void testFindById_NotFound() {
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(id, tenantId));
    }
}
