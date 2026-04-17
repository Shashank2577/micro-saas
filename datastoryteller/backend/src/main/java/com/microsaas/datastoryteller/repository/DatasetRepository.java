package com.microsaas.datastoryteller.repository;

import com.microsaas.datastoryteller.domain.model.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, UUID> {
    List<Dataset> findByTenantId(String tenantId);
    Optional<Dataset> findByIdAndTenantId(UUID id, String tenantId);
    boolean existsByIdAndTenantId(UUID id, String tenantId);
}
