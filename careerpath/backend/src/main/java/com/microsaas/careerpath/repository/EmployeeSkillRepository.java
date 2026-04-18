package com.microsaas.careerpath.repository;

import com.microsaas.careerpath.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, UUID> {
    List<EmployeeSkill> findAllByTenantIdAndEmployeeId(String tenantId, UUID employeeId);
    Optional<EmployeeSkill> findByTenantIdAndEmployeeIdAndSkillId(String tenantId, UUID employeeId, UUID skillId);
}
