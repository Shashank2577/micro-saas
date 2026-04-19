package com.microsaas.videonarrator.repository;

import com.microsaas.videonarrator.model.Transcription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TranscriptionRepository extends JpaRepository<Transcription, UUID> {
    List<Transcription> findAllByProjectIdAndTenantId(UUID projectId, String tenantId);
    Optional<Transcription> findByIdAndTenantId(UUID id, String tenantId);
}
