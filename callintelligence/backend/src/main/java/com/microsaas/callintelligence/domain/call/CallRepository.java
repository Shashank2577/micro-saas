package com.microsaas.callintelligence.domain.call;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CallRepository extends JpaRepository<Call, UUID> {
    Page<Call> findByTenantId(UUID tenantId, Pageable pageable);
    Page<Call> findByTenantIdAndRepId(UUID tenantId, String repId, Pageable pageable);
    Optional<Call> findByIdAndTenantId(UUID id, UUID tenantId);
    List<Call> findByTenantIdAndRepId(UUID tenantId, String repId);
}
