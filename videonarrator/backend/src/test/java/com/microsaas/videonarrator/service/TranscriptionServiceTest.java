package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.domain.SubtitleTrack;
import com.microsaas.videonarrator.domain.Transcription;
import com.microsaas.videonarrator.domain.VideoProject;
import com.microsaas.videonarrator.repository.SubtitleTrackRepository;
import com.microsaas.videonarrator.repository.TranscriptionRepository;
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
class TranscriptionServiceTest {

    @Mock
    private TranscriptionRepository transcriptionRepository;

    @Mock
    private SubtitleTrackRepository subtitleTrackRepository;

    @Mock
    private VideoProcessingService videoProcessingService;

    @InjectMocks
    private TranscriptionService service;

    @Test
    void testStartTranscription() {
        UUID projectId = UUID.randomUUID();
        String tenantId = "tenant1";

        VideoProject project = new VideoProject();
        project.setId(projectId);

        Transcription t = new Transcription();
        t.setId(UUID.randomUUID());
        t.setTenantId(tenantId);

        when(videoProcessingService.getProject(eq(projectId), eq(tenantId))).thenReturn(project);
        when(transcriptionRepository.save(any(Transcription.class))).thenReturn(t);
        when(subtitleTrackRepository.save(any(SubtitleTrack.class))).thenReturn(new SubtitleTrack());

        Transcription result = service.startTranscription(projectId, tenantId, "en");

        assertNotNull(result);
        assertEquals(Transcription.TranscriptionStatus.COMPLETED, result.getStatus());
        verify(transcriptionRepository, times(2)).save(any(Transcription.class));
        verify(subtitleTrackRepository, times(2)).save(any(SubtitleTrack.class));
    }
}
