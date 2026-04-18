package com.microsaas.educationos.domain.repository;

import com.microsaas.educationos.domain.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {
    List<Module> findByCourseIdAndTenantId(UUID courseId, UUID tenantId);
    Optional<Module> findByIdAndTenantId(UUID id, UUID tenantId);
}
