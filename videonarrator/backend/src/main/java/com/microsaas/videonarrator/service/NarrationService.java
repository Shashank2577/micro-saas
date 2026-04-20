package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.domain.NarrationTrack;
import com.microsaas.videonarrator.domain.VideoProject;
import com.microsaas.videonarrator.dto.NarrateRequest;
import com.microsaas.videonarrator.dto.VoiceDto;
import com.microsaas.videonarrator.repository.NarrationTrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NarrationService {

    private final NarrationTrackRepository narrationTrackRepository;
    private final VideoProcessingService videoProcessingService;

    public List<VoiceDto> getVoices() {
        return List.of(
            new VoiceDto("alloy", "OPENAI", "Alloy (OpenAI)"),
            new VoiceDto("echo", "OPENAI", "Echo (OpenAI)"),
            new VoiceDto("en-US-Standard-A", "GOOGLE", "Google US English Standard A"),
            new VoiceDto("eleven_multilingual_v2", "ELEVENLABS", "ElevenLabs Default")
        );
    }

    @Transactional
    public NarrationTrack startNarration(UUID projectId, String tenantId, NarrateRequest request) {
        VideoProject project = videoProcessingService.getProject(projectId, tenantId);

        NarrationTrack track = new NarrationTrack();
        track.setProjectId(project.getId());
        track.setTenantId(tenantId);
        track.setVoiceProvider(NarrationTrack.VoiceProvider.valueOf(request.getVoiceProvider().toUpperCase()));
        track.setVoiceId(request.getVoiceId());
        track.setStatus(NarrationTrack.NarrationStatus.PENDING);

        track = narrationTrackRepository.save(track);

        project.setStatus(VideoProject.ProjectStatus.NARRATING);

        // Mock async processing for now.
        mockNarrationProcess(track);

        return track;
    }

    private void mockNarrationProcess(NarrationTrack track) {
        track.setStatus(NarrationTrack.NarrationStatus.COMPLETED);
        track.setAudioUrl("/api/v1/projects/media/mock-audio.mp3");
        narrationTrackRepository.save(track);
    }
}
