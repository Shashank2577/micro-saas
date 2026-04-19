package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.CalibrationNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface CalibrationNoteRepository extends JpaRepository<CalibrationNote, UUID> {
    List<CalibrationNote> findByTenantId(UUID tenantId);
    Optional<CalibrationNote> findByIdAndTenantId(UUID id, UUID tenantId);
}
