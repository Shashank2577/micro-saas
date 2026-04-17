package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.error.CcException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TenantServiceTest {

    private TenantRepository tenantRepository;
    private TenantService tenantService;

    @BeforeEach
    void setUp() {
        tenantRepository = mock(TenantRepository.class);
        tenantService = new TenantService(tenantRepository);
    }

    @Test
    void getTenantReturnsTenantWhenFound() {
        UUID id = UUID.randomUUID();
        Tenant tenant = new Tenant();
        tenant.setName("Acme");
        tenant.setSlug("acme");
        when(tenantRepository.findById(id)).thenReturn(Optional.of(tenant));

        Tenant result = tenantService.getTenant(id);

        assertThat(result).isSameAs(tenant);
        assertThat(result.getName()).isEqualTo("Acme");
    }

    @Test
    void getTenantThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(tenantRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tenantService.getTenant(id))
                .isInstanceOf(CcException.class)
                .satisfies(ex -> {
                    CcException ccEx = (CcException) ex;
                    assertThat(ccEx.getErrorCode()).isEqualTo("TENANT_NOT_FOUND");
                    assertThat(ccEx.getHttpStatus()).isEqualTo(404);
                });
    }

    @Test
    void getTenantBySlugReturnsTenantWhenFound() {
        Tenant tenant = new Tenant();
        tenant.setName("Acme");
        tenant.setSlug("acme");
        when(tenantRepository.findBySlug("acme")).thenReturn(Optional.of(tenant));

        Tenant result = tenantService.getTenantBySlug("acme");

        assertThat(result).isSameAs(tenant);
        assertThat(result.getSlug()).isEqualTo("acme");
    }

    @Test
    void getTenantBySlugThrowsWhenNotFound() {
        when(tenantRepository.findBySlug("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tenantService.getTenantBySlug("missing"))
                .isInstanceOf(CcException.class)
                .satisfies(ex -> {
                    CcException ccEx = (CcException) ex;
                    assertThat(ccEx.getErrorCode()).isEqualTo("TENANT_NOT_FOUND");
                    assertThat(ccEx.getHttpStatus()).isEqualTo(404);
                });
    }

    @Test
    void getTenantsForUserReturnsList() {
        UUID userId = UUID.randomUUID();
        Tenant t1 = new Tenant();
        t1.setName("Tenant A");
        Tenant t2 = new Tenant();
        t2.setName("Tenant B");
        when(tenantRepository.findByUserId(userId)).thenReturn(List.of(t1, t2));

        List<Tenant> result = tenantService.getTenantsForUser(userId);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(t1, t2);
    }

    @Test
    void createTenantSavesAndReturns() {
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(inv -> inv.getArgument(0));

        Tenant result = tenantService.createTenant("Acme", "acme");

        assertThat(result.getName()).isEqualTo("Acme");
        assertThat(result.getSlug()).isEqualTo("acme");
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    void updateTenantUpdatesNameAndSettings() {
        UUID id = UUID.randomUUID();
        Tenant existing = new Tenant();
        existing.setName("Old Name");
        existing.setSlug("old");
        when(tenantRepository.findById(id)).thenReturn(Optional.of(existing));
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(inv -> inv.getArgument(0));

        Map<String, Object> newSettings = Map.of("theme", "dark");
        Tenant result = tenantService.updateTenant(id, "New Name", newSettings);

        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getSettings()).isEqualTo(newSettings);
        verify(tenantRepository).save(existing);
    }

    @Test
    void updateTenantSkipsNullFields() {
        UUID id = UUID.randomUUID();
        Tenant existing = new Tenant();
        existing.setName("Original");
        existing.setSlug("original");
        when(tenantRepository.findById(id)).thenReturn(Optional.of(existing));
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(inv -> inv.getArgument(0));

        Tenant result = tenantService.updateTenant(id, null, null);

        assertThat(result.getName()).isEqualTo("Original");
        verify(tenantRepository).save(existing);
    }
}
