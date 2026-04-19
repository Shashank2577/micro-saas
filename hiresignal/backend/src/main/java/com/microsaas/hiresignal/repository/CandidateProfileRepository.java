package com.microsaas.hiresignal.repository;

import com.microsaas.hiresignal.model.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, UUID> {
    List<CandidateProfile> findByTenantId(UUID tenantId);
    Optional<CandidateProfile> findByIdAndTenantId(UUID id, UUID tenantId);
}
