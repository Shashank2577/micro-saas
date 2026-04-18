package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.model.NarrationTrack;
import com.microsaas.videonarrator.model.VideoProject;
import com.microsaas.videonarrator.repository.NarrationTrackRepository;
import com.microsaas.videonarrator.repository.VideoProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NarrationService {

    private final NarrationTrackRepository narrationTrackRepository;
    private final VideoProjectRepository videoProjectRepository;

    public List<Map<String, String>> getAvailableVoices() {
        return List.of(
            Map.of("id", "alloy", "provider", "OPENAI", "name", "OpenAI Alloy"),
            Map.of("id", "echo", "provider", "OPENAI", "name", "OpenAI Echo"),
            Map.of("id", "en-US-Standard-A", "provider", "GOOGLE", "name", "Google US English A")
        );
    }

    @Transactional
    public NarrationTrack startNarration(String tenantId, UUID projectId, String voiceProvider, String voiceId, UUID transcriptionId) {
        VideoProject project = videoProjectRepository.findByIdAndTenantId(projectId, tenantId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        NarrationTrack track = new NarrationTrack();
        track.setProjectId(projectId);
        track.setTenantId(tenantId);
        track.setVoiceProvider(voiceProvider);
        track.setVoiceId(voiceId);
        track.setStatus("PENDING");

        project.setStatus("NARRATING");
        videoProjectRepository.save(project);

        NarrationTrack saved = narrationTrackRepository.save(track);

        simulateNarrationJob(saved, project);

        return saved;
    }

    private void simulateNarrationJob(NarrationTrack track, VideoProject project) {
        track.setStatus("COMPLETED");
        track.setAudioUrl("http://localhost:9000/videonarrator-mock/audio-" + UUID.randomUUID() + ".mp3");
        narrationTrackRepository.save(track);

        project.setStatus("READY");
        videoProjectRepository.save(project);
    }

    public List<NarrationTrack> getNarrations(String tenantId, UUID projectId) {
        return narrationTrackRepository.findAllByProjectIdAndTenantId(projectId, tenantId);
    }
}
