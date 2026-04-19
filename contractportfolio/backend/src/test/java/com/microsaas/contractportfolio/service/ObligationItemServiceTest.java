package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ObligationItem;
import com.microsaas.contractportfolio.repository.ObligationItemRepository;
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
class ObligationItemServiceTest {

    @Mock
    private ObligationItemRepository repository;

    @InjectMocks
    private ObligationItemService service;

    @Test
    void testCreate() {
        ObligationItem entity = new ObligationItem();
        when(repository.save(any(ObligationItem.class))).thenReturn(entity);
        ObligationItem result = service.create(entity);
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new ObligationItem()));
        Optional<ObligationItem> result = service.getById(id, tenantId);
        assertTrue(result.isPresent());
    }
}
