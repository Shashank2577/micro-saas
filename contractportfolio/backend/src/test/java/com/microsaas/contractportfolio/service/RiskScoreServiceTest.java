package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.RiskScore;
import com.microsaas.contractportfolio.repository.RiskScoreRepository;
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
class RiskScoreServiceTest {

    @Mock
    private RiskScoreRepository repository;

    @InjectMocks
    private RiskScoreService service;

    @Test
    void testCreate() {
        RiskScore entity = new RiskScore();
        when(repository.save(any(RiskScore.class))).thenReturn(entity);
        RiskScore result = service.create(entity);
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new RiskScore()));
        Optional<RiskScore> result = service.getById(id, tenantId);
        assertTrue(result.isPresent());
    }
}
