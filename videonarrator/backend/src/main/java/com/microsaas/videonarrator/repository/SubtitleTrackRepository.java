package com.microsaas.videonarrator.repository;

import com.microsaas.videonarrator.model.SubtitleTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubtitleTrackRepository extends JpaRepository<SubtitleTrack, UUID> {
    List<SubtitleTrack> findAllByTranscriptionIdAndTenantIdOrderBySequenceOrderAsc(UUID transcriptionId, String tenantId);
    Optional<SubtitleTrack> findByIdAndTenantId(UUID id, String tenantId);
}
