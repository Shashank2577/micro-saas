package com.crosscutting.starter.rbac;

import com.crosscutting.starter.error.CcException;
import com.crosscutting.starter.tenancy.TenantMembershipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RbacServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private TenantMembershipRepository tenantMembershipRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private SetOperations<String, String> setOperations;

    @InjectMocks
    private RbacService rbacService;

    private final UUID userId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();

    private final UUID roleId1 = UUID.randomUUID();
    private final UUID roleId2 = UUID.randomUUID();
    private final UUID permissionId1 = UUID.randomUUID();
    private final UUID roleIdNotFound = UUID.randomUUID();

    private void stubRedisSetOps() {
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
    }

    @Test
    void hasPermission_returnsTrueWhenUserHasPermissionViaRole() {
        stubRedisSetOps();
        String cacheKey = "rbac:" + userId + ":" + tenantId;
        when(setOperations.members(cacheKey)).thenReturn(null);

        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId1);
        when(userRoleRepository.findByUserIdAndTenantId(userId, tenantId))
                .thenReturn(List.of(userRole));

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId1);
        rolePermission.setPermissionId(permissionId1);
        when(rolePermissionRepository.findByRoleIdIn(List.of(roleId1)))
                .thenReturn(List.of(rolePermission));

        Permission permission = new Permission();
        permission.setId(permissionId1);
        permission.setResource("orders");
        permission.setAction("read");
        when(permissionRepository.findAllById(List.of(permissionId1)))
                .thenReturn(List.of(permission));

        boolean result = rbacService.hasPermission(userId, tenantId, "orders", "read");

        assertThat(result).isTrue();
    }

    @Test
    void hasPermission_returnsFalseWhenNoMatchingPermission() {
        stubRedisSetOps();
        String cacheKey = "rbac:" + userId + ":" + tenantId;
        when(setOperations.members(cacheKey)).thenReturn(null);

        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId1);
        when(userRoleRepository.findByUserIdAndTenantId(userId, tenantId))
                .thenReturn(List.of(userRole));

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId1);
        rolePermission.setPermissionId(permissionId1);
        when(rolePermissionRepository.findByRoleIdIn(List.of(roleId1)))
                .thenReturn(List.of(rolePermission));

        Permission permission = new Permission();
        permission.setId(permissionId1);
        permission.setResource("orders");
        permission.setAction("read");
        when(permissionRepository.findAllById(List.of(permissionId1)))
                .thenReturn(List.of(permission));

        boolean result = rbacService.hasPermission(userId, tenantId, "orders", "delete");

        assertThat(result).isFalse();
    }

    @Test
    void hasPermission_returnsTrueForWildcardPermission() {
        stubRedisSetOps();
        String cacheKey = "rbac:" + userId + ":" + tenantId;
        when(setOperations.members(cacheKey)).thenReturn(Set.of("*:*"));

        boolean result = rbacService.hasPermission(userId, tenantId, "anything", "any_action");

        assertThat(result).isTrue();
    }

    @Test
    void hasPermission_usesRedisCacheWhenAvailable() {
        stubRedisSetOps();
        String cacheKey = "rbac:" + userId + ":" + tenantId;
        when(setOperations.members(cacheKey)).thenReturn(Set.of("orders:read", "orders:write"));

        boolean result = rbacService.hasPermission(userId, tenantId, "orders", "read");

        assertThat(result).isTrue();
        // Verify DB was never queried since cache was used
        org.mockito.Mockito.verifyNoInteractions(userRoleRepository, rolePermissionRepository, permissionRepository);
    }

    @Test
    void hasPermission_returnsFalseWhenUserHasNoRoles() {
        stubRedisSetOps();
        String cacheKey = "rbac:" + userId + ":" + tenantId;
        when(setOperations.members(cacheKey)).thenReturn(null);
        when(userRoleRepository.findByUserIdAndTenantId(userId, tenantId))
                .thenReturn(List.of());

        boolean result = rbacService.hasPermission(userId, tenantId, "orders", "read");

        assertThat(result).isFalse();
    }

    @Test
    void getUserRoles_returnsRolesForTenant() {
        UserRole ur1 = new UserRole();
        ur1.setRoleId(roleId1);
        UserRole ur2 = new UserRole();
        ur2.setRoleId(roleId2);
        when(userRoleRepository.findByUserIdAndTenantId(userId, tenantId))
                .thenReturn(List.of(ur1, ur2));

        Role role1 = new Role();
        role1.setId(roleId1);
        role1.setName("admin");
        Role role2 = new Role();
        role2.setId(roleId2);
        role2.setName("viewer");
        when(roleRepository.findAllById(List.of(roleId1, roleId2)))
                .thenReturn(List.of(role1, role2));

        List<Role> roles = rbacService.getUserRoles(userId, tenantId);

        assertThat(roles).hasSize(2);
        assertThat(roles).extracting(Role::getName).containsExactly("admin", "viewer");
    }

    @Test
    void assignRole_createsUserRoleMapping() {
        Role role = new Role();
        role.setId(roleId1);
        role.setName("editor");
        when(roleRepository.findById(roleId1)).thenReturn(Optional.of(role));
        when(tenantMembershipRepository.existsByUserIdAndTenantId(userId, tenantId)).thenReturn(true);
        when(userRoleRepository.findByUserIdAndTenantId(userId, tenantId))
                .thenReturn(List.of());

        rbacService.assignRole(userId, roleId1, tenantId);

        verify(userRoleRepository).save(any(UserRole.class));
        verify(redisTemplate).delete("rbac:" + userId + ":" + tenantId);
    }

    @Test
    void assignRole_throwsWhenRoleNotFound() {
        when(roleRepository.findById(roleIdNotFound)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rbacService.assignRole(userId, roleIdNotFound, tenantId))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "RESOURCE_NOT_FOUND");
    }

    @Test
    void assignRole_throwsWhenRoleAlreadyAssigned() {
        Role role = new Role();
        role.setId(roleId1);
        when(roleRepository.findById(roleId1)).thenReturn(Optional.of(role));
        when(tenantMembershipRepository.existsByUserIdAndTenantId(userId, tenantId)).thenReturn(true);

        UserRole existing = new UserRole();
        existing.setRoleId(roleId1);
        when(userRoleRepository.findByUserIdAndTenantId(userId, tenantId))
                .thenReturn(List.of(existing));

        assertThatThrownBy(() -> rbacService.assignRole(userId, roleId1, tenantId))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "RESOURCE_CONFLICT");
    }

    @Test
    void assignRole_throwsWhenUserNotMemberOfTenant() {
        Role role = new Role();
        role.setId(roleId1);
        when(roleRepository.findById(roleId1)).thenReturn(Optional.of(role));
        when(tenantMembershipRepository.existsByUserIdAndTenantId(userId, tenantId)).thenReturn(false);

        assertThatThrownBy(() -> rbacService.assignRole(userId, roleId1, tenantId))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "FORBIDDEN");
    }

    @Test
    void removeRole_deletesAndInvalidatesCache() {
        rbacService.removeRole(userId, roleId1, tenantId);

        verify(userRoleRepository).deleteByUserIdAndRoleIdAndTenantId(userId, roleId1, tenantId);
        verify(redisTemplate).delete("rbac:" + userId + ":" + tenantId);
    }

    @Test
    void getPermissions_cachesResultInRedis() {
        stubRedisSetOps();
        String cacheKey = "rbac:" + userId + ":" + tenantId;
        when(setOperations.members(cacheKey)).thenReturn(null);

        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId1);
        when(userRoleRepository.findByUserIdAndTenantId(userId, tenantId))
                .thenReturn(List.of(userRole));

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId1);
        rolePermission.setPermissionId(permissionId1);
        when(rolePermissionRepository.findByRoleIdIn(List.of(roleId1)))
                .thenReturn(List.of(rolePermission));

        Permission permission = new Permission();
        permission.setId(permissionId1);
        permission.setResource("orders");
        permission.setAction("read");
        when(permissionRepository.findAllById(List.of(permissionId1)))
                .thenReturn(List.of(permission));

        Set<String> result = rbacService.getPermissions(userId, tenantId);

        assertThat(result).containsExactly("orders:read");
        verify(setOperations).add(eq(cacheKey), any(String[].class));
        verify(redisTemplate).expire(cacheKey, Duration.ofMinutes(5));
    }
}
