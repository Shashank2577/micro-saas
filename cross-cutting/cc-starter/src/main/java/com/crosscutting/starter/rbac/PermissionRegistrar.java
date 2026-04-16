package com.crosscutting.starter.rbac;

import com.crosscutting.starter.CcProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;

@Component
public class PermissionRegistrar implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PermissionRegistrar.class);
    private static final String CACHE_PREFIX = "rbac:";

    private final CcProperties ccProperties;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final StringRedisTemplate redisTemplate;

    public PermissionRegistrar(CcProperties ccProperties,
                               PermissionRepository permissionRepository,
                               RoleRepository roleRepository,
                               RolePermissionRepository rolePermissionRepository,
                               StringRedisTemplate redisTemplate) {
        this.ccProperties = ccProperties;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<CcProperties.RbacProperties.AppPermission> appPermissions =
                ccProperties.getRbac().getAppPermissions();

        if (appPermissions.isEmpty()) {
            return;
        }

        log.info("Registering {} app permission groups", appPermissions.size());

        // Find system roles for auto-mapping
        Role orgAdmin = roleRepository.findByNameAndIsSystemTrue("org_admin").orElse(null);
        Role member = roleRepository.findByNameAndIsSystemTrue("member").orElse(null);

        for (CcProperties.RbacProperties.AppPermission ap : appPermissions) {
            for (String action : ap.getActions()) {
                // Create permission if it doesn't exist
                Permission permission = permissionRepository
                        .findByResourceAndAction(ap.getResource(), action)
                        .orElseGet(() -> {
                            Permission p = new Permission();
                            p.setResource(ap.getResource());
                            p.setAction(action);
                            p.setDescription("Auto-registered: " + ap.getResource() + "." + action);
                            Permission saved = permissionRepository.save(p);
                            log.info("Registered permission: {}.{}", ap.getResource(), action);
                            return saved;
                        });

                // Map to org_admin (gets all permissions)
                if (orgAdmin != null) {
                    mapPermissionToRole(orgAdmin, permission);
                }

                // Map read permissions to member role
                if (member != null && "read".equals(action)) {
                    mapPermissionToRole(member, permission);
                }
            }
        }

        // Invalidate all RBAC caches since new permissions were registered
        Set<String> keys = redisTemplate.keys(CACHE_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Invalidated {} RBAC cache entries after permission registration", keys.size());
        }
    }

    private void mapPermissionToRole(Role role, Permission permission) {
        RolePermissionId rpId = new RolePermissionId(role.getId(), permission.getId());
        if (!rolePermissionRepository.existsById(rpId)) {
            RolePermission rp = new RolePermission();
            rp.setRoleId(role.getId());
            rp.setPermissionId(permission.getId());
            rolePermissionRepository.save(rp);
            log.debug("Mapped {}.{} to role {}", permission.getResource(), permission.getAction(), role.getName());
        }
    }
}
