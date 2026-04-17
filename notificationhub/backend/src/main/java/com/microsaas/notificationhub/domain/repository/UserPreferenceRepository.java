package com.microsaas.notificationhub.domain.repository;

import com.microsaas.notificationhub.domain.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, UUID> {
    List<UserPreference> findByTenantIdAndUserId(String tenantId, String userId);
    Optional<UserPreference> findByTenantIdAndUserIdAndChannel(String tenantId, String userId, String channel);
}
