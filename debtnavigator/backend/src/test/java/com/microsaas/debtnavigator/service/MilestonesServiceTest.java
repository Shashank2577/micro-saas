package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.MilestoneTrack;
import com.microsaas.debtnavigator.repository.MilestoneTrackRepository;
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
class MilestonesServiceTest {

    @Mock
    private MilestoneTrackRepository repository;

    @InjectMocks
    private MilestonesService service;

    @Test
    void testCreate() {
        MilestoneTrack track = new MilestoneTrack();
        when(repository.save(any(MilestoneTrack.class))).thenReturn(track);
        assertNotNull(service.create(new MilestoneTrack()));
        verify(repository).save(any(MilestoneTrack.class));
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new MilestoneTrack()));
        assertTrue(service.getById(id, tenantId).isPresent());
    }
}
