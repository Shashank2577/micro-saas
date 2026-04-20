package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.domain.SubtitleTrack;
import com.microsaas.videonarrator.domain.Transcription;
import com.microsaas.videonarrator.domain.VideoProject;
import com.microsaas.videonarrator.dto.SubtitleUpdateRequest;
import com.microsaas.videonarrator.repository.SubtitleTrackRepository;
import com.microsaas.videonarrator.repository.TranscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranscriptionService {

    private final TranscriptionRepository transcriptionRepository;
    private final SubtitleTrackRepository subtitleTrackRepository;
    private final VideoProcessingService videoProcessingService;

    @Transactional
    public Transcription startTranscription(UUID projectId, String tenantId, String languageCode) {
        VideoProject project = videoProcessingService.getProject(projectId, tenantId);

        Transcription transcription = new Transcription();
        transcription.setProjectId(project.getId());
        transcription.setTenantId(tenantId);
        transcription.setLanguageCode(languageCode);
        transcription.setStatus(Transcription.TranscriptionStatus.PENDING);

        transcription = transcriptionRepository.save(transcription);

        project.setStatus(VideoProject.ProjectStatus.TRANSCRIBING);

        // Mock async processing for now.
        mockTranscriptionProcess(transcription);

        return transcription;
    }

    public List<Transcription> getTranscriptions(UUID projectId, String tenantId) {
        return transcriptionRepository.findAllByProjectIdAndTenantId(projectId, tenantId);
    }

    public List<SubtitleTrack> getSubtitles(UUID transcriptionId, String tenantId) {
        return subtitleTrackRepository.findAllByTranscriptionIdAndTenantIdOrderBySequenceOrderAsc(transcriptionId, tenantId);
    }

    @Transactional
    public SubtitleTrack updateSubtitle(UUID subtitleId, String tenantId, SubtitleUpdateRequest request) {
        SubtitleTrack track = subtitleTrackRepository.findByIdAndTenantId(subtitleId, tenantId)
                .orElseThrow(() -> new RuntimeException("Subtitle not found"));

        track.setStartTimeMs(request.getStartTimeMs());
        track.setEndTimeMs(request.getEndTimeMs());
        track.setContent(request.getContent());

        return subtitleTrackRepository.save(track);
    }

    private void mockTranscriptionProcess(Transcription transcription) {
        transcription.setStatus(Transcription.TranscriptionStatus.COMPLETED);
        transcription.setFullText("This is a mocked transcription.");
        transcriptionRepository.save(transcription);

        SubtitleTrack track1 = new SubtitleTrack();
        track1.setTranscriptionId(transcription.getId());
        track1.setTenantId(transcription.getTenantId());
        track1.setStartTimeMs(0L);
        track1.setEndTimeMs(2000L);
        track1.setContent("This is");
        track1.setSequenceOrder(1);
        subtitleTrackRepository.save(track1);

        SubtitleTrack track2 = new SubtitleTrack();
        track2.setTranscriptionId(transcription.getId());
        track2.setTenantId(transcription.getTenantId());
        track2.setStartTimeMs(2000L);
        track2.setEndTimeMs(4000L);
        track2.setContent("a mocked transcription.");
        track2.setSequenceOrder(2);
        subtitleTrackRepository.save(track2);
    }
}
