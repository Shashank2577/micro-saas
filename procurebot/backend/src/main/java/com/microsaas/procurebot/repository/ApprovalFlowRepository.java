package com.microsaas.procurebot.repository;

import com.microsaas.procurebot.model.ApprovalFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalFlowRepository extends JpaRepository<ApprovalFlow, UUID> {
    List<ApprovalFlow> findByTenantId(UUID tenantId);
    Optional<ApprovalFlow> findByIdAndTenantId(UUID id, UUID tenantId);
}
