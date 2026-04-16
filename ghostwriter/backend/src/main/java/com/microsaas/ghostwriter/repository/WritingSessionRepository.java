package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.WritingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WritingSessionRepository extends JpaRepository<WritingSession, UUID> {
    List<WritingSession> findByTenantId(UUID tenantId);
    Optional<WritingSession> findByIdAndTenantId(UUID id, UUID tenantId);
}
