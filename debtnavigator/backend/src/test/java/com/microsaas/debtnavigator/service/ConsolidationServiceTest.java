package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.ConsolidationOffer;
import com.microsaas.debtnavigator.repository.ConsolidationOfferRepository;
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
class ConsolidationServiceTest {

    @Mock
    private ConsolidationOfferRepository repository;

    @InjectMocks
    private ConsolidationService service;

    @Test
    void testCreate() {
        ConsolidationOffer offer = new ConsolidationOffer();
        when(repository.save(any(ConsolidationOffer.class))).thenReturn(offer);
        assertNotNull(service.create(new ConsolidationOffer()));
        verify(repository).save(any(ConsolidationOffer.class));
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new ConsolidationOffer()));
        assertTrue(service.getById(id, tenantId).isPresent());
    }
}
