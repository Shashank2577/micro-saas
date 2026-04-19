package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.DeprecationNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeprecationNoticeRepository extends JpaRepository<DeprecationNotice, UUID> {
    List<DeprecationNotice> findByTenantId(UUID tenantId);
    Optional<DeprecationNotice> findByIdAndTenantId(UUID id, UUID tenantId);
}
