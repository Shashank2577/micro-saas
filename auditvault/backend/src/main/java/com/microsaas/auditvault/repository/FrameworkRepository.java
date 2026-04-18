package com.microsaas.auditvault.repository;

import com.microsaas.auditvault.model.Framework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FrameworkRepository extends JpaRepository<Framework, UUID> {
    List<Framework> findByTenantId(UUID tenantId);
}
