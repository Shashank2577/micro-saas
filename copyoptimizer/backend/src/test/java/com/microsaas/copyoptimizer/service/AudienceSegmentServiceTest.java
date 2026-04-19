package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.AudienceSegment;
import com.microsaas.copyoptimizer.repository.AudienceSegmentRepository;
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
class AudienceSegmentServiceTest {

    @Mock
    private AudienceSegmentRepository repository;

    @InjectMocks
    private AudienceSegmentService service;

    @Test
    void create_ShouldSaveAndReturnEntity() {
        AudienceSegment entity = new AudienceSegment();
        entity.setName("Test Audience");

        when(repository.save(any(AudienceSegment.class))).thenReturn(entity);

        AudienceSegment result = service.create(entity);

        assertNotNull(result);
        assertEquals("Test Audience", result.getName());
    }

    @Test
    void getById_WhenExists_ShouldReturnEntity() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        AudienceSegment entity = new AudienceSegment();
        entity.setId(id);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(entity));

        AudienceSegment result = service.getById(id, tenantId);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }
}
