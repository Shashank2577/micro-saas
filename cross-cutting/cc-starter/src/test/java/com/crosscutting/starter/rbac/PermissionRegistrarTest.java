package com.crosscutting.starter.rbac;

import com.crosscutting.starter.CcProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionRegistrarTest {

    @Mock private CcProperties ccProperties;
    @Mock private PermissionRepository permissionRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private RolePermissionRepository rolePermissionRepository;
    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ApplicationArguments args;

    private PermissionRegistrar registrar;

    @BeforeEach
    void setUp() {
        registrar = new PermissionRegistrar(ccProperties, permissionRepository,
                roleRepository, rolePermissionRepository, redisTemplate);
    }

    private CcProperties.RbacProperties rbacWithPermissions(List<CcProperties.RbacProperties.AppPermission> perms) {
        CcProperties.RbacProperties rbac = new CcProperties.RbacProperties();
        rbac.setAppPermissions(perms);
        return rbac;
    }

    @Test
    void run_skipsWhenNoAppPermissionsConfigured() throws Exception {
        when(ccProperties.getRbac()).thenReturn(rbacWithPermissions(Collections.emptyList()));

        registrar.run(args);

        verifyNoInteractions(permissionRepository, roleRepository, rolePermissionRepository, redisTemplate);
    }

    @Test
    void run_registersNewPermissions() throws Exception {
        CcProperties.RbacProperties.AppPermission ap = new CcProperties.RbacProperties.AppPermission();
        ap.setResource("todo");
        ap.setActions(List.of("read", "write"));

        when(ccProperties.getRbac()).thenReturn(rbacWithPermissions(List.of(ap)));
        when(roleRepository.findByNameAndIsSystemTrue("org_admin")).thenReturn(Optional.empty());
        when(roleRepository.findByNameAndIsSystemTrue("member")).thenReturn(Optional.empty());
        when(permissionRepository.findByResourceAndAction(eq("todo"), anyString()))
                .thenReturn(Optional.empty());
        when(permissionRepository.save(any(Permission.class))).thenAnswer(inv -> {
            Permission p = inv.getArgument(0);
            p.setId(UUID.randomUUID());
            return p;
        });
        when(redisTemplate.keys("rbac:*")).thenReturn(Collections.emptySet());

        registrar.run(args);

        verify(permissionRepository, times(2)).save(any(Permission.class));
    }

    @Test
    void run_doesNotDuplicateExistingPermissions() throws Exception {
        Permission existing = new Permission();
        existing.setId(UUID.randomUUID());
        existing.setResource("todo");
        existing.setAction("read");

        CcProperties.RbacProperties.AppPermission ap = new CcProperties.RbacProperties.AppPermission();
        ap.setResource("todo");
        ap.setActions(List.of("read"));

        when(ccProperties.getRbac()).thenReturn(rbacWithPermissions(List.of(ap)));
        when(roleRepository.findByNameAndIsSystemTrue("org_admin")).thenReturn(Optional.empty());
        when(roleRepository.findByNameAndIsSystemTrue("member")).thenReturn(Optional.empty());
        when(permissionRepository.findByResourceAndAction("todo", "read"))
                .thenReturn(Optional.of(existing));
        when(redisTemplate.keys("rbac:*")).thenReturn(Collections.emptySet());

        registrar.run(args);

        verify(permissionRepository, never()).save(any(Permission.class));
    }

    @Test
    void run_mapsAllPermissionsToOrgAdmin() throws Exception {
        Role orgAdmin = new Role();
        orgAdmin.setId(UUID.randomUUID());
        orgAdmin.setName("org_admin");
        orgAdmin.setSystem(true);

        Permission perm = new Permission();
        perm.setId(UUID.randomUUID());
        perm.setResource("todo");
        perm.setAction("write");

        CcProperties.RbacProperties.AppPermission ap = new CcProperties.RbacProperties.AppPermission();
        ap.setResource("todo");
        ap.setActions(List.of("write"));

        when(ccProperties.getRbac()).thenReturn(rbacWithPermissions(List.of(ap)));
        when(roleRepository.findByNameAndIsSystemTrue("org_admin")).thenReturn(Optional.of(orgAdmin));
        when(roleRepository.findByNameAndIsSystemTrue("member")).thenReturn(Optional.empty());
        when(permissionRepository.findByResourceAndAction("todo", "write"))
                .thenReturn(Optional.of(perm));
        when(rolePermissionRepository.existsById(any(RolePermissionId.class))).thenReturn(false);
        when(redisTemplate.keys("rbac:*")).thenReturn(Collections.emptySet());

        registrar.run(args);

        verify(rolePermissionRepository).save(any(RolePermission.class));
    }

    @Test
    void run_mapsReadPermissionsToMemberRole() throws Exception {
        Role member = new Role();
        member.setId(UUID.randomUUID());
        member.setName("member");
        member.setSystem(true);

        Permission perm = new Permission();
        perm.setId(UUID.randomUUID());
        perm.setResource("todo");
        perm.setAction("read");

        CcProperties.RbacProperties.AppPermission ap = new CcProperties.RbacProperties.AppPermission();
        ap.setResource("todo");
        ap.setActions(List.of("read"));

        when(ccProperties.getRbac()).thenReturn(rbacWithPermissions(List.of(ap)));
        when(roleRepository.findByNameAndIsSystemTrue("org_admin")).thenReturn(Optional.empty());
        when(roleRepository.findByNameAndIsSystemTrue("member")).thenReturn(Optional.of(member));
        when(permissionRepository.findByResourceAndAction("todo", "read"))
                .thenReturn(Optional.of(perm));
        when(rolePermissionRepository.existsById(any(RolePermissionId.class))).thenReturn(false);
        when(redisTemplate.keys("rbac:*")).thenReturn(Collections.emptySet());

        registrar.run(args);

        verify(rolePermissionRepository).save(any(RolePermission.class));
    }

    @Test
    void run_doesNotMapWritePermissionsToMemberRole() throws Exception {
        Role member = new Role();
        member.setId(UUID.randomUUID());
        member.setName("member");
        member.setSystem(true);

        Permission perm = new Permission();
        perm.setId(UUID.randomUUID());
        perm.setResource("todo");
        perm.setAction("write");

        CcProperties.RbacProperties.AppPermission ap = new CcProperties.RbacProperties.AppPermission();
        ap.setResource("todo");
        ap.setActions(List.of("write"));

        when(ccProperties.getRbac()).thenReturn(rbacWithPermissions(List.of(ap)));
        when(roleRepository.findByNameAndIsSystemTrue("org_admin")).thenReturn(Optional.empty());
        when(roleRepository.findByNameAndIsSystemTrue("member")).thenReturn(Optional.of(member));
        when(permissionRepository.findByResourceAndAction("todo", "write"))
                .thenReturn(Optional.of(perm));
        when(redisTemplate.keys("rbac:*")).thenReturn(Collections.emptySet());

        registrar.run(args);

        verify(rolePermissionRepository, never()).save(any(RolePermission.class));
    }

    @Test
    void run_invalidatesCacheAfterRegistration() throws Exception {
        CcProperties.RbacProperties.AppPermission ap = new CcProperties.RbacProperties.AppPermission();
        ap.setResource("todo");
        ap.setActions(List.of("read"));

        when(ccProperties.getRbac()).thenReturn(rbacWithPermissions(List.of(ap)));
        when(roleRepository.findByNameAndIsSystemTrue("org_admin")).thenReturn(Optional.empty());
        when(roleRepository.findByNameAndIsSystemTrue("member")).thenReturn(Optional.empty());
        when(permissionRepository.findByResourceAndAction("todo", "read"))
                .thenReturn(Optional.empty());
        when(permissionRepository.save(any(Permission.class))).thenAnswer(inv -> {
            Permission p = inv.getArgument(0);
            p.setId(UUID.randomUUID());
            return p;
        });
        when(redisTemplate.keys("rbac:*")).thenReturn(Set.of("rbac:user1:tenant1"));

        registrar.run(args);

        verify(redisTemplate).delete(Set.of("rbac:user1:tenant1"));
    }
}
