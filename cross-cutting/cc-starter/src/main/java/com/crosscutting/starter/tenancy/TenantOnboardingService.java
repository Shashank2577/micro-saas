package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.error.CcErrorCodes;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

public class TenantOnboardingService {

    private static final Logger log = LoggerFactory.getLogger(TenantOnboardingService.class);

    private final TenantRepository tenantRepository;
    private final EntityManager entityManager;

    public TenantOnboardingService(TenantRepository tenantRepository, EntityManager entityManager) {
        this.tenantRepository = tenantRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Tenant onboardTenant(String name, String slug, UUID adminUserId) {
        // 1. Create tenant
        Tenant tenant = new Tenant();
        tenant.setName(name);
        tenant.setSlug(slug);
        tenant = tenantRepository.save(tenant);

        UUID tenantId = tenant.getId();

        // 2. Create membership for admin user
        TenantMembership membership = new TenantMembership();
        membership.setTenantId(tenantId);
        membership.setUserId(adminUserId);
        membership.setJoinedAt(Instant.now());
        entityManager.persist(membership);

        // 3. Assign org_admin role to admin user for this tenant
        UUID roleId = (UUID) entityManager
                .createNativeQuery("SELECT id FROM cc.roles WHERE name = 'org_admin' AND (tenant_id IS NULL OR tenant_id = :tenantId) ORDER BY tenant_id NULLS LAST LIMIT 1")
                .setParameter("tenantId", tenantId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (roleId != null) {
            entityManager.createNativeQuery(
                            "INSERT INTO cc.user_roles (user_id, role_id, tenant_id, assigned_at) VALUES (:userId, :roleId, :tenantId, :assignedAt)")
                    .setParameter("userId", adminUserId)
                    .setParameter("roleId", roleId)
                    .setParameter("tenantId", tenantId)
                    .setParameter("assignedAt", Instant.now())
                    .executeUpdate();
        } else {
            log.warn("org_admin role not found during tenant onboarding for tenant {}. " +
                    "Admin user {} will not have the org_admin role assigned. " +
                    "Ensure the role is seeded via V2__rbac.sql or V9__seed_role_permissions.sql.",
                    tenantId, adminUserId);
        }

        // 4. Create default tenant_config
        entityManager.createNativeQuery(
                        "INSERT INTO cc.tenant_config (tenant_id, key, value) VALUES (:tenantId, 'default', CAST(:value AS jsonb))")
                .setParameter("tenantId", tenantId)
                .setParameter("value", "{}")
                .executeUpdate();

        return tenant;
    }

    /**
     * Onboard an existing tenant by ID: create membership, assign admin role, and create default config.
     * Unlike {@link #onboardTenant}, this does NOT create a new tenant — it uses the one identified by {@code tenantId}.
     */
    @Transactional
    public Tenant onboardExistingTenant(UUID tenantId, UUID adminUserId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> CcErrorCodes.tenantNotFound("Tenant not found: " + tenantId));

        // Create membership for admin user
        TenantMembership membership = new TenantMembership();
        membership.setTenantId(tenantId);
        membership.setUserId(adminUserId);
        membership.setJoinedAt(Instant.now());
        entityManager.persist(membership);

        // Assign org_admin role to admin user for this tenant
        UUID roleId = (UUID) entityManager
                .createNativeQuery("SELECT id FROM cc.roles WHERE name = 'org_admin' AND (tenant_id IS NULL OR tenant_id = :tenantId) ORDER BY tenant_id NULLS LAST LIMIT 1")
                .setParameter("tenantId", tenantId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (roleId != null) {
            entityManager.createNativeQuery(
                            "INSERT INTO cc.user_roles (user_id, role_id, tenant_id, assigned_at) VALUES (:userId, :roleId, :tenantId, :assignedAt)")
                    .setParameter("userId", adminUserId)
                    .setParameter("roleId", roleId)
                    .setParameter("tenantId", tenantId)
                    .setParameter("assignedAt", Instant.now())
                    .executeUpdate();
        } else {
            log.warn("org_admin role not found during tenant onboarding for existing tenant {}. " +
                    "Admin user {} will not have the org_admin role assigned. " +
                    "Ensure the role is seeded via V2__rbac.sql or V9__seed_role_permissions.sql.",
                    tenantId, adminUserId);
        }

        // Create default tenant_config
        entityManager.createNativeQuery(
                        "INSERT INTO cc.tenant_config (tenant_id, key, value) VALUES (:tenantId, 'default', CAST(:value AS jsonb))")
                .setParameter("tenantId", tenantId)
                .setParameter("value", "{}")
                .executeUpdate();

        return tenant;
    }
}
