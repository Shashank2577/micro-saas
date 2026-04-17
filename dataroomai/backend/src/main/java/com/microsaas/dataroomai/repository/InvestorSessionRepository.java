package com.microsaas.dataroomai.repository;

import com.microsaas.dataroomai.domain.InvestorSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvestorSessionRepository extends JpaRepository<InvestorSession, UUID> {
    List<InvestorSession> findByRoomIdAndTenantId(UUID roomId, UUID tenantId);
    Optional<InvestorSession> findByIdAndTenantId(UUID id, UUID tenantId);
}
