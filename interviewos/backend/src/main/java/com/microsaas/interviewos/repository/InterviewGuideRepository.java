package com.microsaas.interviewos.repository;

import com.microsaas.interviewos.model.InterviewGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewGuideRepository extends JpaRepository<InterviewGuide, UUID> {
    List<InterviewGuide> findByTenantId(UUID tenantId);
}
