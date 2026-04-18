package com.microsaas.authvault.repository;

import com.microsaas.authvault.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuthVaultTenantRepository extends JpaRepository<Tenant, UUID> {
}
