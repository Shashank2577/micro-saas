package com.microsaas.ndaflow.repository;

import com.microsaas.ndaflow.domain.NdaRedline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NdaRedlineRepository extends JpaRepository<NdaRedline, UUID> {
    List<NdaRedline> findByNdaIdAndTenantId(UUID ndaId, UUID tenantId);
}
