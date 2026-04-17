package com.microsaas.apigatekeeper.controller;

import com.microsaas.apigatekeeper.entity.AnalyticsSnapshot;
import com.microsaas.apigatekeeper.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping("/traffic")
    public ResponseEntity<List<AnalyticsSnapshot>> getTraffic(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getTraffic(tenantId));
    }
}
