package com.microsaas.educationos.domain.repository;

import com.microsaas.educationos.domain.entity.LearnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LearnerProfileRepository extends JpaRepository<LearnerProfile, UUID> {
    Optional<LearnerProfile> findByUserIdAndTenantId(UUID userId, UUID tenantId);
}
