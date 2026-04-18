package com.microsaas.careerpath.repository;

import com.microsaas.careerpath.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {
    List<Skill> findAllByTenantId(String tenantId);
    Optional<Skill> findByIdAndTenantId(UUID id, String tenantId);
}
