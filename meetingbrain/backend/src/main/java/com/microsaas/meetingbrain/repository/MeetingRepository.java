package com.microsaas.meetingbrain.repository;

import com.microsaas.meetingbrain.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    List<Meeting> findByTenantId(UUID tenantId);
}
