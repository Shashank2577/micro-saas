package com.microsaas.careerpath.repository;

import com.microsaas.careerpath.entity.DevelopmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DevelopmentPlanRepository extends JpaRepository<DevelopmentPlan, UUID> {
    List<DevelopmentPlan> findAllByTenantIdAndEmployeeId(String tenantId, UUID employeeId);
}
