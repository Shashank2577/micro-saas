package com.microsaas.interviewos.repository;

import com.microsaas.interviewos.model.InterviewScorecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewScorecardRepository extends JpaRepository<InterviewScorecard, UUID> {
    List<InterviewScorecard> findByInterviewerIdAndTenantId(UUID interviewerId, UUID tenantId);
    List<InterviewScorecard> findByTenantId(UUID tenantId);
}
