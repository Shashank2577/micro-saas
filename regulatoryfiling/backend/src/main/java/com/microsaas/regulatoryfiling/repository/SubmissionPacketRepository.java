package com.microsaas.regulatoryfiling.repository;

import com.microsaas.regulatoryfiling.domain.SubmissionPacket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubmissionPacketRepository extends JpaRepository<SubmissionPacket, UUID> {
    List<SubmissionPacket> findByTenantId(UUID tenantId);
    Optional<SubmissionPacket> findByIdAndTenantId(UUID id, UUID tenantId);
}
