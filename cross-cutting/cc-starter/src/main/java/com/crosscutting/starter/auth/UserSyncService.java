package com.crosscutting.starter.auth;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class UserSyncService {

    private static final Logger log = LoggerFactory.getLogger(UserSyncService.class);
    private static final UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public UserSyncService(UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public User syncUser(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");
        String displayName = jwt.getClaimAsString("name");
        String avatarUrl = jwt.getClaimAsString("picture");

        Optional<User> existing = userRepository.findById(userId);

        User result;
        if (existing.isPresent()) {
            User user = existing.get();
            user.setLastLoginAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            log.debug("Updated last_login_at for user {}", userId);
            result = userRepository.save(user);
        } else {
            User user = new User();
            user.setId(userId);
            user.setEmail(email);
            user.setDisplayName(displayName);
            user.setAvatarUrl(avatarUrl);
            user.setStatus("active");
            Instant now = Instant.now();
            user.setLastLoginAt(now);
            user.setCreatedAt(now);
            user.setUpdatedAt(now);
            log.info("Created new user record for {} ({})", userId, email);
            result = userRepository.save(user);
        }

        // Auto-onboard: if user has no tenant membership, assign to default tenant
        autoOnboardIfNeeded(userId);

        return result;
    }

    /**
     * Returns the default tenant ID if the user has a membership in the default tenant.
     * This is used by UserSyncFilter to set tenant context when no X-Tenant-ID header is present.
     */
    public UUID getDefaultTenantIdForUser(UUID userId) {
        Number count = (Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM cc.tenant_memberships WHERE user_id = :userId AND tenant_id = :tenantId")
                .setParameter("userId", userId)
                .setParameter("tenantId", DEFAULT_TENANT_ID)
                .getSingleResult();
        return count.intValue() > 0 ? DEFAULT_TENANT_ID : null;
    }

    private void autoOnboardIfNeeded(UUID userId) {
        // Check if user has any tenant membership
        Number count = (Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM cc.tenant_memberships WHERE user_id = :userId")
                .setParameter("userId", userId)
                .getSingleResult();
        boolean hasMembership = count.intValue() > 0;

        if (!hasMembership) {
            // Assign to default tenant
            entityManager.createNativeQuery(
                    "INSERT INTO cc.tenant_memberships (id, tenant_id, user_id, joined_at) VALUES (gen_random_uuid(), :tenantId, :userId, NOW()) ON CONFLICT DO NOTHING")
                    .setParameter("tenantId", DEFAULT_TENANT_ID)
                    .setParameter("userId", userId)
                    .executeUpdate();

            // Assign member role
            entityManager.createNativeQuery(
                    "INSERT INTO cc.user_roles (user_id, role_id, tenant_id, assigned_at) SELECT :userId, id, :tenantId, NOW() FROM cc.roles WHERE name = 'member' AND is_system = TRUE ON CONFLICT DO NOTHING")
                    .setParameter("userId", userId)
                    .setParameter("tenantId", DEFAULT_TENANT_ID)
                    .executeUpdate();

            log.info("Auto-onboarded user {} to default tenant", userId);
        }
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}
