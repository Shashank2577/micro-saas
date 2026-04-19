package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.CounterpartyProfile;
import com.microsaas.contractportfolio.repository.CounterpartyProfileRepository;
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
class CounterpartyProfileServiceTest {

    @Mock
    private CounterpartyProfileRepository repository;

    @InjectMocks
    private CounterpartyProfileService service;

    @Test
    void testCreate() {
        CounterpartyProfile entity = new CounterpartyProfile();
        when(repository.save(any(CounterpartyProfile.class))).thenReturn(entity);
        CounterpartyProfile result = service.create(entity);
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new CounterpartyProfile()));
        Optional<CounterpartyProfile> result = service.getById(id, tenantId);
        assertTrue(result.isPresent());
    }
}
