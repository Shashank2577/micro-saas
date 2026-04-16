package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.SkillsGap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillsGapRepository extends JpaRepository<SkillsGap, UUID> {
    List<SkillsGap> findByTenantId(UUID tenantId);
    List<SkillsGap> findByDepartmentAndTenantId(String department, UUID tenantId);
}
