package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.error.CcErrorCodes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public Page<Tenant> listTenants(Pageable pageable) {
        return tenantRepository.findAll(pageable);
    }

    public Tenant getTenant(UUID id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> CcErrorCodes.tenantNotFound("Tenant not found: " + id));
    }

    public Tenant getTenantBySlug(String slug) {
        return tenantRepository.findBySlug(slug)
                .orElseThrow(() -> CcErrorCodes.tenantNotFound("Tenant not found with slug: " + slug));
    }

    public List<Tenant> getTenantsForUser(UUID userId) {
        return tenantRepository.findByUserId(userId);
    }

    public Tenant createTenant(String name, String slug) {
        Tenant tenant = new Tenant();
        tenant.setName(name);
        tenant.setSlug(slug);
        return tenantRepository.save(tenant);
    }

    public Tenant updateTenant(UUID id, String name, Map<String, Object> settings) {
        Tenant tenant = getTenant(id);
        if (name != null) {
            tenant.setName(name);
        }
        if (settings != null) {
            tenant.setSettings(settings);
        }
        return tenantRepository.save(tenant);
    }
}
