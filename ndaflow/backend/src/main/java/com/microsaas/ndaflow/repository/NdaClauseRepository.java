package com.microsaas.ndaflow.repository;

import com.microsaas.ndaflow.domain.NdaClause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NdaClauseRepository extends JpaRepository<NdaClause, UUID> {
    List<NdaClause> findByNdaIdAndTenantId(UUID ndaId, UUID tenantId);
}
