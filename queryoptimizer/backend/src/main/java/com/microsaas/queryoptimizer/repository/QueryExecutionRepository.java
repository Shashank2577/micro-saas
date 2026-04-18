package com.microsaas.queryoptimizer.repository;

import com.microsaas.queryoptimizer.domain.QueryExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QueryExecutionRepository extends JpaRepository<QueryExecution, UUID> {
    List<QueryExecution> findByTenantIdAndFingerprintId(UUID tenantId, UUID fingerprintId);

    @Query("SELECT AVG(e.executionTimeMs) FROM QueryExecution e WHERE e.tenantId = :tenantId AND e.fingerprint.id = :fingerprintId")
    Double getAverageExecutionTimeMs(@Param("tenantId") UUID tenantId, @Param("fingerprintId") UUID fingerprintId);

    @Query("SELECT COUNT(e) FROM QueryExecution e WHERE e.tenantId = :tenantId AND e.fingerprint.id = :fingerprintId")
    Long getExecutionCount(@Param("tenantId") UUID tenantId, @Param("fingerprintId") UUID fingerprintId);
}
