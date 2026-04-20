package com.microsaas.videonarrator.repository;

import com.microsaas.videonarrator.domain.NarrationTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NarrationTrackRepository extends JpaRepository<NarrationTrack, UUID> {
    List<NarrationTrack> findAllByProjectIdAndTenantId(UUID projectId, String tenantId);
    Optional<NarrationTrack> findByIdAndTenantId(UUID id, String tenantId);
}
