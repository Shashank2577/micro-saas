package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.PredictionScore;
import com.microsaas.copyoptimizer.repository.PredictionScoreRepository;
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
class PredictionScoreServiceTest {

    @Mock
    private PredictionScoreRepository repository;

    @InjectMocks
    private PredictionScoreService service;

    @Test
    void create_ShouldSaveAndReturnEntity() {
        PredictionScore entity = new PredictionScore();
        entity.setName("Test Score");

        when(repository.save(any(PredictionScore.class))).thenReturn(entity);

        PredictionScore result = service.create(entity);

        assertNotNull(result);
        assertEquals("Test Score", result.getName());
    }

    @Test
    void getById_WhenExists_ShouldReturnEntity() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        PredictionScore entity = new PredictionScore();
        entity.setId(id);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(entity));

        PredictionScore result = service.getById(id, tenantId);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }
}
