package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.OnCallSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnCallScheduleRepository extends JpaRepository<OnCallSchedule, UUID> {
    List<OnCallSchedule> findByTenantId(String tenantId);
    Optional<OnCallSchedule> findByIdAndTenantId(UUID id, String tenantId);
}
