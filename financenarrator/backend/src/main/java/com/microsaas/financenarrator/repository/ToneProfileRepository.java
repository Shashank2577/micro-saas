package com.microsaas.financenarrator.repository;

import com.microsaas.financenarrator.model.ToneProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ToneProfileRepository extends JpaRepository<ToneProfile, UUID> {
    List<ToneProfile> findByTenantId(UUID tenantId);
    Optional<ToneProfile> findByIdAndTenantId(UUID id, UUID tenantId);
}
