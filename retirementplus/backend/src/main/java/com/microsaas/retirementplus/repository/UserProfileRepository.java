package com.microsaas.retirementplus.repository;

import com.microsaas.retirementplus.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUserIdAndTenantId(UUID userId, UUID tenantId);
}
