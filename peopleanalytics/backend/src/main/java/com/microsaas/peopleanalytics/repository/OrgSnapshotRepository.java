package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.OrgSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrgSnapshotRepository extends JpaRepository<OrgSnapshot, UUID> {
    List<OrgSnapshot> findByTenantId(UUID tenantId);
    Optional<OrgSnapshot> findByIdAndTenantId(UUID id, UUID tenantId);
}
