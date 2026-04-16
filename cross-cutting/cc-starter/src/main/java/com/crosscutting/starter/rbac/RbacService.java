package com.crosscutting.starter.rbac;

import com.crosscutting.starter.error.CcErrorCodes;
import com.crosscutting.starter.tenancy.TenantMembershipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RbacService {

    private static final Logger log = LoggerFactory.getLogger(RbacService.class);
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);
    private static final String CACHE_PREFIX = "rbac:";
    private static final String WILDCARD_PERMISSION = "*:*";

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final TenantMembershipRepository tenantMembershipRepository;
    private final StringRedisTemplate redisTemplate;

    public RbacService(UserRoleRepository userRoleRepository,
                       RoleRepository roleRepository,
                       RolePermissionRepository rolePermissionRepository,
                       PermissionRepository permissionRepository,
                       TenantMembershipRepository tenantMembershipRepository,
                       StringRedisTemplate redisTemplate) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
        this.tenantMembershipRepository = tenantMembershipRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Check if a user has a specific permission within a tenant.
     * Uses Redis caching with 5-minute TTL.
     */
    public boolean hasPermission(UUID userId, UUID tenantId, String resource, String action) {
        Set<String> permissions = getPermissions(userId, tenantId);
        String required = resource + ":" + action;
        return permissions.contains(WILDCARD_PERMISSION)
                || permissions.contains(required)
                || permissions.contains(resource + ":*")
                || permissions.contains("*:" + action);
    }

    /**
     * Get all permissions for a user in a tenant.
     * Returns permissions as "resource:action" strings.
     */
    public Set<String> getPermissions(UUID userId, UUID tenantId) {
        String cacheKey = CACHE_PREFIX + userId + ":" + tenantId;

        // Check Redis cache first
        Set<String> cached = redisTemplate.opsForSet().members(cacheKey);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        // Cache miss: query DB
        Set<String> permissions = loadPermissionsFromDb(userId, tenantId);

        // Cache in Redis with TTL
        if (!permissions.isEmpty()) {
            redisTemplate.opsForSet().add(cacheKey, permissions.toArray(new String[0]));
            redisTemplate.expire(cacheKey, CACHE_TTL);
        }

        return permissions;
    }

    private Set<String> loadPermissionsFromDb(UUID userId, UUID tenantId) {
        // Get user's roles in this tenant
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndTenantId(userId, tenantId);
        if (userRoles.isEmpty()) {
            return Set.of();
        }

        List<UUID> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());

        // Get permission IDs for those roles
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleIdIn(roleIds);
        if (rolePermissions.isEmpty()) {
            return Set.of();
        }

        List<UUID> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        // Load permissions
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        Set<String> result = new HashSet<>();
        for (Permission p : permissions) {
            result.add(p.getResource() + ":" + p.getAction());
        }
        return result;
    }

    /**
     * Assign a role to a user within a tenant.
     */
    @Transactional
    public void assignRole(UUID userId, UUID roleId, UUID tenantId) {
        // Verify role exists
        roleRepository.findById(roleId)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Role not found: " + roleId));

        // Verify target user is a member of this tenant
        if (!tenantMembershipRepository.existsByUserIdAndTenantId(userId, tenantId)) {
            throw CcErrorCodes.forbidden(
                    "User " + userId + " is not a member of tenant " + tenantId);
        }

        // Check for duplicate assignment
        List<UserRole> existing = userRoleRepository.findByUserIdAndTenantId(userId, tenantId);
        boolean alreadyAssigned = existing.stream().anyMatch(ur -> ur.getRoleId().equals(roleId));
        if (alreadyAssigned) {
            throw CcErrorCodes.resourceConflict("Role already assigned to user");
        }

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setTenantId(tenantId);
        userRoleRepository.save(userRole);

        invalidateCache(userId, tenantId);
        log.info("Assigned role {} to user {} in tenant {}", roleId, userId, tenantId);
    }

    /**
     * Remove a role from a user within a tenant.
     */
    @Transactional
    public void removeRole(UUID userId, UUID roleId, UUID tenantId) {
        userRoleRepository.deleteByUserIdAndRoleIdAndTenantId(userId, roleId, tenantId);
        invalidateCache(userId, tenantId);
        log.info("Removed role {} from user {} in tenant {}", roleId, userId, tenantId);
    }

    /**
     * Get all roles assigned to a user in a tenant.
     */
    public List<Role> getUserRoles(UUID userId, UUID tenantId) {
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndTenantId(userId, tenantId);
        List<UUID> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        return roleRepository.findAllById(roleIds);
    }

    /**
     * Create a custom (non-system) role scoped to a tenant.
     */
    @Transactional
    public Role createRole(UUID tenantId, String name, String description, List<UUID> permissionIds) {
        Role role = new Role();
        role.setTenantId(tenantId);
        role.setName(name);
        role.setDescription(description);
        role.setSystem(false);
        role = roleRepository.save(role);

        // Associate permissions
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (UUID permId : permissionIds) {
                // Verify permission exists
                permissionRepository.findById(permId)
                        .orElseThrow(() -> CcErrorCodes.resourceNotFound("Permission not found: " + permId));

                RolePermission rp = new RolePermission();
                rp.setRoleId(role.getId());
                rp.setPermissionId(permId);
                rolePermissionRepository.save(rp);
            }
        }

        log.info("Created role '{}' (id={}) in tenant {}", name, role.getId(), tenantId);
        return role;
    }

    /**
     * Delete a custom role. System roles cannot be deleted.
     */
    @Transactional
    public void deleteRole(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Role not found: " + roleId));

        if (role.isSystem()) {
            throw CcErrorCodes.forbidden("Cannot delete system role: " + role.getName());
        }

        rolePermissionRepository.deleteByRoleId(roleId);
        roleRepository.delete(role);
        log.info("Deleted role '{}' (id={})", role.getName(), roleId);
    }

    /**
     * Invalidate the cached permissions for a user in a tenant.
     */
    public void invalidateCache(UUID userId, UUID tenantId) {
        String cacheKey = CACHE_PREFIX + userId + ":" + tenantId;
        redisTemplate.delete(cacheKey);
        log.debug("Invalidated RBAC cache for user {} in tenant {}", userId, tenantId);
    }
}
