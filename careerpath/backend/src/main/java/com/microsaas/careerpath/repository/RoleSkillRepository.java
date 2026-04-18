package com.microsaas.careerpath.repository;

import com.microsaas.careerpath.entity.RoleSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleSkillRepository extends JpaRepository<RoleSkill, UUID> {
    List<RoleSkill> findAllByTenantIdAndRoleId(String tenantId, UUID roleId);
}
