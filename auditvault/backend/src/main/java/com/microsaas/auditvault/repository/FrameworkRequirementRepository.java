package com.microsaas.auditvault.repository;

import com.microsaas.auditvault.model.FrameworkRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FrameworkRequirementRepository extends JpaRepository<FrameworkRequirement, UUID> {
    List<FrameworkRequirement> findByTenantId(UUID tenantId);
    List<FrameworkRequirement> findByTenantIdAndFrameworkId(UUID tenantId, UUID frameworkId);
}
