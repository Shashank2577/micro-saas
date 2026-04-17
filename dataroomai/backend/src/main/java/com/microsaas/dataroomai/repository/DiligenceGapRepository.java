package com.microsaas.dataroomai.repository;

import com.microsaas.dataroomai.domain.DiligenceGap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiligenceGapRepository extends JpaRepository<DiligenceGap, UUID> {
    List<DiligenceGap> findByRoomIdAndTenantId(UUID roomId, UUID tenantId);
    Optional<DiligenceGap> findByIdAndTenantId(UUID id, UUID tenantId);
}
