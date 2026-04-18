package com.microsaas.restaurantintel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.restaurantintel.domain.StaffSchedule;
import com.microsaas.restaurantintel.repository.StaffScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StaffSchedulingService {

    private final StaffScheduleRepository scheduleRepository;

    public StaffSchedulingService(StaffScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<StaffSchedule> getSchedules() {
        return scheduleRepository.findByTenantId(TenantContext.require());
    }

    public void generateSchedules() {
        UUID tenantId = TenantContext.require();
        // Simplified generation
        StaffSchedule schedule = new StaffSchedule();
        schedule.setTenantId(tenantId);
        schedule.setRole("Server");
        schedule.setDate(LocalDate.now().plusDays(1));
        schedule.setShiftStart(LocalTime.of(17, 0));
        schedule.setShiftEnd(LocalTime.of(23, 0));
        schedule.setPredictedBusyness("HIGH");
        schedule.setRecommendedStaffCount(4);
        scheduleRepository.save(schedule);
    }
}
