package com.microsaas.integrationbridge.repository;

import com.microsaas.integrationbridge.model.ApiCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiCredentialRepository extends JpaRepository<ApiCredential, UUID> {
    Optional<ApiCredential> findByIntegrationIdAndTenantId(UUID integrationId, UUID tenantId);
}
