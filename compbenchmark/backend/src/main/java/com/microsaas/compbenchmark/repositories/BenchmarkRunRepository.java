package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.BenchmarkRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BenchmarkRunRepository extends JpaRepository<BenchmarkRun, UUID> {
    List<BenchmarkRun> findByTenantId(UUID tenantId);
    Optional<BenchmarkRun> findByIdAndTenantId(UUID id, UUID tenantId);
}
