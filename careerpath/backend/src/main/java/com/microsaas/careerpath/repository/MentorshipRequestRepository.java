package com.microsaas.careerpath.repository;

import com.microsaas.careerpath.entity.MentorshipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MentorshipRequestRepository extends JpaRepository<MentorshipRequest, UUID> {
    List<MentorshipRequest> findAllByTenantIdAndMenteeId(String tenantId, UUID menteeId);
    List<MentorshipRequest> findAllByTenantIdAndMentorId(String tenantId, UUID mentorId);
}
