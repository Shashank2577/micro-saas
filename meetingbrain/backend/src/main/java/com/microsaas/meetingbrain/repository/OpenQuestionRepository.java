package com.microsaas.meetingbrain.repository;

import com.microsaas.meetingbrain.model.OpenQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OpenQuestionRepository extends JpaRepository<OpenQuestion, UUID> {
    List<OpenQuestion> findByTenantIdAndMeetingId(UUID tenantId, UUID meetingId);
    List<OpenQuestion> findByTenantId(UUID tenantId);
}
