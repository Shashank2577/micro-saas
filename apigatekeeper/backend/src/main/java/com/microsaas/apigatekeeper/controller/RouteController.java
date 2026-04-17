package com.microsaas.apigatekeeper.controller;

import com.microsaas.apigatekeeper.entity.Route;
import com.microsaas.apigatekeeper.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gateway/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<Route> createRoute(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody Route route) {
        return ResponseEntity.status(HttpStatus.CREATED).body(routeService.createRoute(tenantId, route));
    }

    @GetMapping
    public ResponseEntity<List<Route>> getRoutes(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(routeService.getRoutes(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID id) {
        return routeService.getRoute(tenantId, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID id,
            @RequestBody Route route) {
        try {
            return ResponseEntity.ok(routeService.updateRoute(tenantId, id, route));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID id) {
        try {
            routeService.deleteRoute(tenantId, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
