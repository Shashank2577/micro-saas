package com.microsaas.processminer.repository;

import com.microsaas.processminer.domain.ProcessModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProcessModelRepository extends JpaRepository<ProcessModel, UUID> {
    List<ProcessModel> findByTenantId(UUID tenantId);
    Optional<ProcessModel> findByIdAndTenantId(UUID id, UUID tenantId);
}
