package com.microsaas.hiresignal.repository;

import com.microsaas.hiresignal.model.FitSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FitSignalRepository extends JpaRepository<FitSignal, UUID> {
    List<FitSignal> findByTenantId(UUID tenantId);
    Optional<FitSignal> findByIdAndTenantId(UUID id, UUID tenantId);
}
