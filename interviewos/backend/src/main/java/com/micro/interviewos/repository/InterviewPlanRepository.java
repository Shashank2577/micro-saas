package com.micro.interviewos.repository;

import com.micro.interviewos.domain.InterviewPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterviewPlanRepository extends JpaRepository<InterviewPlan, UUID> {
    List<InterviewPlan> findByTenantId(UUID tenantId);
    Optional<InterviewPlan> findByIdAndTenantId(UUID id, UUID tenantId);
}
