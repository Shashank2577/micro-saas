package com.microsaas.retentionsignal.repository;

import com.microsaas.retentionsignal.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, UUID> {
    List<EmployeeProfile> findByTenantId(UUID tenantId);
}
