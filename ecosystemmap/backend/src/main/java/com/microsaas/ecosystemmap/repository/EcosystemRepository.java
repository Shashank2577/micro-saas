package com.microsaas.ecosystemmap.repository;

import com.microsaas.ecosystemmap.entity.Ecosystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EcosystemRepository extends JpaRepository<Ecosystem, UUID> {
    List<Ecosystem> findByTenantId(String tenantId);
    Optional<Ecosystem> findByIdAndTenantId(UUID id, String tenantId);
}
