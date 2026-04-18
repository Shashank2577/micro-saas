package com.microsaas.prospectiq.repository;

import com.microsaas.prospectiq.model.ICPProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICPProfileRepository extends JpaRepository<ICPProfile, UUID> {
    List<ICPProfile> findByTenantId(UUID tenantId);
    Optional<ICPProfile> findByIdAndTenantId(UUID id, UUID tenantId);
}
