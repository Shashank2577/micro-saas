package com.microsaas.retentionsignal.repository;

import com.microsaas.retentionsignal.model.RetentionIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RetentionInterventionRepository extends JpaRepository<RetentionIntervention, UUID> {
    List<RetentionIntervention> findByEmployeeIdAndTenantId(UUID employeeId, UUID tenantId);
    List<RetentionIntervention> findByTenantId(UUID tenantId);
}
