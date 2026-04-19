package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.model.SubtitleTrack;
import com.microsaas.videonarrator.model.Transcription;
import com.microsaas.videonarrator.model.VideoProject;
import com.microsaas.videonarrator.repository.SubtitleTrackRepository;
import com.microsaas.videonarrator.repository.TranscriptionRepository;
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

class TranscriptionServiceTest {

    @Mock
    private TranscriptionRepository transcriptionRepository;
    @Mock
    private SubtitleTrackRepository subtitleTrackRepository;
    @Mock
    private VideoProjectRepository videoProjectRepository;

    @InjectMocks
    private TranscriptionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartTranscription() {
        UUID projectId = UUID.randomUUID();
        VideoProject project = new VideoProject();
        project.setId(projectId);

        Transcription t = new Transcription();
        t.setId(UUID.randomUUID());
        t.setTenantId("tenant1");

        when(videoProjectRepository.findByIdAndTenantId(projectId, "tenant1")).thenReturn(Optional.of(project));
        when(transcriptionRepository.save(any(Transcription.class))).thenReturn(t);
        when(subtitleTrackRepository.save(any(SubtitleTrack.class))).thenReturn(new SubtitleTrack());

        Transcription result = service.startTranscription("tenant1", projectId, "en");

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(transcriptionRepository, times(2)).save(any(Transcription.class));
        verify(subtitleTrackRepository, times(2)).save(any(SubtitleTrack.class));
    }
}
