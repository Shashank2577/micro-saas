package com.microsaas.customerdiscoveryai.repository;

import com.microsaas.customerdiscoveryai.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    List<Interview> findByProjectIdAndTenantId(UUID projectId, UUID tenantId);
    Optional<Interview> findByIdAndTenantId(UUID id, UUID tenantId);
}
