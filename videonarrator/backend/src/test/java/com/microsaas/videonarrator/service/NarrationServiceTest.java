package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.model.NarrationTrack;
import com.microsaas.videonarrator.model.VideoProject;
import com.microsaas.videonarrator.repository.NarrationTrackRepository;
import com.microsaas.videonarrator.repository.VideoProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NarrationServiceTest {

    @Mock
    private NarrationTrackRepository narrationTrackRepository;
    @Mock
    private VideoProjectRepository videoProjectRepository;

    @InjectMocks
    private NarrationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartNarration() {
        UUID projectId = UUID.randomUUID();
        VideoProject project = new VideoProject();
        project.setId(projectId);

        NarrationTrack track = new NarrationTrack();
        track.setId(UUID.randomUUID());
        track.setTenantId("tenant1");

        when(videoProjectRepository.findByIdAndTenantId(projectId, "tenant1")).thenReturn(Optional.of(project));
        when(narrationTrackRepository.save(any(NarrationTrack.class))).thenReturn(track);

        NarrationTrack result = service.startNarration("tenant1", projectId, "OPENAI", "alloy", UUID.randomUUID());

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(narrationTrackRepository, times(2)).save(any(NarrationTrack.class));
        verify(videoProjectRepository, times(2)).save(any(VideoProject.class));
    }
}
