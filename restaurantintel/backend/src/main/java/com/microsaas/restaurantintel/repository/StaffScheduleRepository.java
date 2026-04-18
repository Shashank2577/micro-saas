package com.microsaas.restaurantintel.repository;

import com.microsaas.restaurantintel.domain.StaffSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffScheduleRepository extends JpaRepository<StaffSchedule, UUID> {
    List<StaffSchedule> findByTenantId(UUID tenantId);
}
