package com.micro.interviewos.repository;

import com.micro.interviewos.domain.CalibrationSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalibrationSignalRepository extends JpaRepository<CalibrationSignal, UUID> {
    List<CalibrationSignal> findByTenantId(UUID tenantId);
    Optional<CalibrationSignal> findByIdAndTenantId(UUID id, UUID tenantId);
}
