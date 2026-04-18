package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.model.SubtitleTrack;
import com.microsaas.videonarrator.model.Transcription;
import com.microsaas.videonarrator.model.VideoProject;
import com.microsaas.videonarrator.repository.SubtitleTrackRepository;
import com.microsaas.videonarrator.repository.TranscriptionRepository;
import com.microsaas.videonarrator.repository.VideoProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranscriptionService {

    private final TranscriptionRepository transcriptionRepository;
    private final SubtitleTrackRepository subtitleTrackRepository;
    private final VideoProjectRepository videoProjectRepository;

    @Transactional
    public Transcription startTranscription(String tenantId, UUID projectId, String languageCode) {
        VideoProject project = videoProjectRepository.findByIdAndTenantId(projectId, tenantId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Transcription transcription = new Transcription();
        transcription.setProjectId(projectId);
        transcription.setTenantId(tenantId);
        transcription.setLanguageCode(languageCode);
        transcription.setStatus("PENDING");

        project.setStatus("TRANSCRIBING");
        videoProjectRepository.save(project);

        Transcription saved = transcriptionRepository.save(transcription);

        simulateTranscriptionJob(saved);

        return saved;
    }

    private void simulateTranscriptionJob(Transcription transcription) {
        transcription.setStatus("COMPLETED");
        transcription.setFullText("This is a simulated transcription.");
        transcriptionRepository.save(transcription);

        SubtitleTrack track1 = new SubtitleTrack();
        track1.setTranscriptionId(transcription.getId());
        track1.setTenantId(transcription.getTenantId());
        track1.setStartTimeMs(0L);
        track1.setEndTimeMs(3000L);
        track1.setContent("This is a");
        track1.setSequenceOrder(1);
        subtitleTrackRepository.save(track1);

        SubtitleTrack track2 = new SubtitleTrack();
        track2.setTranscriptionId(transcription.getId());
        track2.setTenantId(transcription.getTenantId());
        track2.setStartTimeMs(3000L);
        track2.setEndTimeMs(6000L);
        track2.setContent("simulated transcription.");
        track2.setSequenceOrder(2);
        subtitleTrackRepository.save(track2);
    }

    public List<Transcription> getTranscriptions(String tenantId, UUID projectId) {
        return transcriptionRepository.findAllByProjectIdAndTenantId(projectId, tenantId);
    }

    public List<SubtitleTrack> getSubtitles(String tenantId, UUID transcriptionId) {
        return subtitleTrackRepository.findAllByTranscriptionIdAndTenantIdOrderBySequenceOrderAsc(transcriptionId, tenantId);
    }

    @Transactional
    public SubtitleTrack updateSubtitle(String tenantId, UUID subtitleId, Long startMs, Long endMs, String content) {
        SubtitleTrack track = subtitleTrackRepository.findByIdAndTenantId(subtitleId, tenantId)
                .orElseThrow(() -> new RuntimeException("Subtitle not found"));

        track.setStartTimeMs(startMs);
        track.setEndTimeMs(endMs);
        track.setContent(content);

        return subtitleTrackRepository.save(track);
    }
}
