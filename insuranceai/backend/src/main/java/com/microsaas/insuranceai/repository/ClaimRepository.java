package com.microsaas.insuranceai.repository;

import com.microsaas.insuranceai.domain.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findByTenantId(UUID tenantId);
    Optional<Claim> findByIdAndTenantId(UUID id, UUID tenantId);
}
