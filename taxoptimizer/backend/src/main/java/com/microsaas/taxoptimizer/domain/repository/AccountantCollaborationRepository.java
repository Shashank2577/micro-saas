package com.microsaas.taxoptimizer.domain.repository;

import com.microsaas.taxoptimizer.domain.entity.AccountantCollaboration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountantCollaborationRepository extends JpaRepository<AccountantCollaboration, UUID> {
    List<AccountantCollaboration> findByTenantIdAndProfileId(UUID tenantId, UUID profileId);
    Optional<AccountantCollaboration> findByTenantIdAndId(UUID tenantId, UUID id);
}
