package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.MemoDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemoDraftRepository extends JpaRepository<MemoDraft, UUID> {
    List<MemoDraft> findByTenantId(UUID tenantId);
    Optional<MemoDraft> findByIdAndTenantId(UUID id, UUID tenantId);
}
