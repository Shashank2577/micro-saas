package com.microsaas.meetingbrain.repository;

import com.microsaas.meetingbrain.model.TranscriptLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TranscriptLineRepository extends JpaRepository<TranscriptLine, UUID> {
    List<TranscriptLine> findByTenantIdAndMeetingIdOrderByStartTimestampAsc(UUID tenantId, UUID meetingId);
}
