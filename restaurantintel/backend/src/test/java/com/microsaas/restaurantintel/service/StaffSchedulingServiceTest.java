package com.microsaas.restaurantintel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.restaurantintel.domain.StaffSchedule;
import com.microsaas.restaurantintel.repository.StaffScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffSchedulingServiceTest {

    @Mock
    private StaffScheduleRepository scheduleRepository;

    @InjectMocks
    private StaffSchedulingService staffSchedulingService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testGetSchedules() {
        StaffSchedule schedule = new StaffSchedule();
        schedule.setRole("Manager");
        when(scheduleRepository.findByTenantId(tenantId)).thenReturn(List.of(schedule));

        List<StaffSchedule> schedules = staffSchedulingService.getSchedules();
        assertEquals(1, schedules.size());
        assertEquals("Manager", schedules.get(0).getRole());
    }

    @Test
    void testGenerateSchedules() {
        when(scheduleRepository.save(any(StaffSchedule.class))).thenAnswer(i -> i.getArguments()[0]);
        staffSchedulingService.generateSchedules();
        verify(scheduleRepository, times(1)).save(any(StaffSchedule.class));
    }
}
