package com.crosscutting.starter.flags;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FlagOverrideRepository extends JpaRepository<FlagOverride, UUID> {

    Optional<FlagOverride> findByFlagIdAndTenantIdAndUserId(UUID flagId, UUID tenantId, UUID userId);

    Optional<FlagOverride> findByFlagIdAndTenantIdAndUserIdIsNull(UUID flagId, UUID tenantId);

    Optional<FlagOverride> findByFlagIdAndTenantIdIsNullAndUserIdIsNull(UUID flagId);
}
