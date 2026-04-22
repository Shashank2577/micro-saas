package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LicenseRepository extends JpaRepository<License, UUID> {
    List<License> findByTenantId(UUID tenantId);
}
