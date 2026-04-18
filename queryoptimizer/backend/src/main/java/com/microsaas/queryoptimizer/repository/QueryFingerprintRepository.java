package com.microsaas.queryoptimizer.repository;

import com.microsaas.queryoptimizer.domain.QueryFingerprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QueryFingerprintRepository extends JpaRepository<QueryFingerprint, UUID> {
    Optional<QueryFingerprint> findByTenantIdAndFingerprintHash(UUID tenantId, String fingerprintHash);
    Optional<QueryFingerprint> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<QueryFingerprint> findByTenantId(UUID tenantId, Pageable pageable);
}
