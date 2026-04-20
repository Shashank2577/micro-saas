package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.domain.NarrationTrack;
import com.microsaas.videonarrator.domain.VideoProject;
import com.microsaas.videonarrator.dto.NarrateRequest;
import com.microsaas.videonarrator.repository.NarrationTrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NarrationServiceTest {

    @Mock
    private NarrationTrackRepository narrationTrackRepository;

    @Mock
    private VideoProcessingService videoProcessingService;

    @InjectMocks
    private NarrationService service;

    @Test
    void testStartNarration() {
        UUID projectId = UUID.randomUUID();
        String tenantId = "tenant1";

        VideoProject project = new VideoProject();
        project.setId(projectId);

        NarrationTrack track = new NarrationTrack();
        track.setId(UUID.randomUUID());
        track.setTenantId(tenantId);

        NarrateRequest request = new NarrateRequest();
        request.setVoiceProvider("OPENAI");
        request.setVoiceId("alloy");

        when(videoProcessingService.getProject(eq(projectId), eq(tenantId))).thenReturn(project);
        when(narrationTrackRepository.save(any(NarrationTrack.class))).thenReturn(track);

        NarrationTrack result = service.startNarration(projectId, tenantId, request);

        assertNotNull(result);
        assertEquals(NarrationTrack.NarrationStatus.COMPLETED, result.getStatus());
        verify(narrationTrackRepository, times(2)).save(any(NarrationTrack.class));
    }
}
