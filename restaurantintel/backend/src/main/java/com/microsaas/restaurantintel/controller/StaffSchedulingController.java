package com.microsaas.restaurantintel.controller;

import com.microsaas.restaurantintel.domain.StaffSchedule;
import com.microsaas.restaurantintel.service.StaffSchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
public class StaffSchedulingController {

    private final StaffSchedulingService service;

    public StaffSchedulingController(StaffSchedulingService service) {
        this.service = service;
    }

    @GetMapping
    public List<StaffSchedule> getSchedules() {
        return service.getSchedules();
    }

    @PostMapping("/generate")
    public ResponseEntity<Void> generateSchedules() {
        service.generateSchedules();
        return ResponseEntity.ok().build();
    }
}
