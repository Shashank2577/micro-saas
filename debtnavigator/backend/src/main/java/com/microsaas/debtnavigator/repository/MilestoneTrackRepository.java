package com.microsaas.debtnavigator.repository;

import com.microsaas.debtnavigator.entity.MilestoneTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MilestoneTrackRepository extends JpaRepository<MilestoneTrack, UUID> {
    List<MilestoneTrack> findByTenantId(UUID tenantId);
    Optional<MilestoneTrack> findByIdAndTenantId(UUID id, UUID tenantId);
}
