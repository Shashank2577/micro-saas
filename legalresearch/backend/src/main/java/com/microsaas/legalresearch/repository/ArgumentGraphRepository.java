package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.ArgumentGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArgumentGraphRepository extends JpaRepository<ArgumentGraph, UUID> {
    List<ArgumentGraph> findByTenantId(UUID tenantId);
    Optional<ArgumentGraph> findByIdAndTenantId(UUID id, UUID tenantId);
}
