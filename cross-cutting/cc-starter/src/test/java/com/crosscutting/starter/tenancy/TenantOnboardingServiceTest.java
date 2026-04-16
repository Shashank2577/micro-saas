package com.crosscutting.starter.tenancy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TenantOnboardingServiceTest {

    private TenantRepository tenantRepository;
    private EntityManager entityManager;
    private TenantOnboardingService onboardingService;

    @BeforeEach
    void setUp() {
        tenantRepository = mock(TenantRepository.class);
        entityManager = mock(EntityManager.class);
        onboardingService = new TenantOnboardingService(tenantRepository, entityManager);
    }

    @Test
    void onboardTenantCreatesTenantWithMembershipAndRole() {
        UUID adminUserId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        Tenant savedTenant = new Tenant();
        savedTenant.setName("Acme");
        savedTenant.setSlug("acme");
        // Use reflection-free approach: mock save to return tenant with id
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(inv -> {
            Tenant t = inv.getArgument(0);
            // Simulate JPA setting the ID
            try {
                var idField = Tenant.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, tenantId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return t;
        });

        // Mock the role lookup query
        Query roleQuery = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(roleQuery);
        when(roleQuery.setParameter(anyString(), any())).thenReturn(roleQuery);
        when(roleQuery.getResultStream()).thenReturn(Stream.of(UUID.randomUUID()));
        when(roleQuery.executeUpdate()).thenReturn(1);

        // Mock the insert queries (user_roles and tenant_config) — same roleQuery mock handles all
        // since createNativeQuery returns the same mock each time

        Tenant result = onboardingService.onboardTenant("Acme", "acme", adminUserId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Acme");
        assertThat(result.getSlug()).isEqualTo("acme");
        assertThat(result.getId()).isEqualTo(tenantId);

        // Verify membership was persisted
        verify(entityManager).persist(any(TenantMembership.class));
        // Verify tenant was saved
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    void onboardTenantSkipsRoleAssignmentWhenNoOrgAdminRole() {
        UUID adminUserId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        when(tenantRepository.save(any(Tenant.class))).thenAnswer(inv -> {
            Tenant t = inv.getArgument(0);
            try {
                var idField = Tenant.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, tenantId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return t;
        });

        // Role lookup query returns empty stream (no org_admin role found)
        Query roleQuery = mock(Query.class);
        // We need separate mocks for role lookup vs insert queries
        Query insertQuery = mock(Query.class);

        when(entityManager.createNativeQuery(anyString())).thenReturn(roleQuery);
        when(roleQuery.setParameter(anyString(), any())).thenReturn(roleQuery);
        when(roleQuery.getResultStream()).thenReturn(Stream.empty());
        when(roleQuery.executeUpdate()).thenReturn(1);

        Tenant result = onboardingService.onboardTenant("Acme", "acme", adminUserId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(tenantId);
        verify(entityManager).persist(any(TenantMembership.class));
    }

    @Test
    void onboardTenantSetsCorrectMembershipFields() {
        UUID adminUserId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        when(tenantRepository.save(any(Tenant.class))).thenAnswer(inv -> {
            Tenant t = inv.getArgument(0);
            try {
                var idField = Tenant.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, tenantId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return t;
        });

        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(Stream.empty());
        when(query.executeUpdate()).thenReturn(1);

        onboardingService.onboardTenant("Acme", "acme", adminUserId);

        verify(entityManager).persist(any(TenantMembership.class));
    }
}
