package com.microsaas.apigatekeeper.service;

import com.microsaas.apigatekeeper.entity.Route;
import com.microsaas.apigatekeeper.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    @Transactional
    public Route createRoute(String tenantId, Route route) {
        route.setTenantId(tenantId);
        route.setCreatedAt(ZonedDateTime.now());
        route.setUpdatedAt(ZonedDateTime.now());
        return routeRepository.save(route);
    }

    public List<Route> getRoutes(String tenantId) {
        return routeRepository.findByTenantId(tenantId);
    }

    public Optional<Route> getRoute(String tenantId, UUID id) {
        return routeRepository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Route updateRoute(String tenantId, UUID id, Route routeDetails) {
        Route route = routeRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        route.setRouteId(routeDetails.getRouteId());
        route.setPath(routeDetails.getPath());
        route.setMethod(routeDetails.getMethod());
        route.setTargetUrl(routeDetails.getTargetUrl());
        route.setStripPrefix(routeDetails.getStripPrefix());
        route.setUpdatedAt(ZonedDateTime.now());

        return routeRepository.save(route);
    }

    @Transactional
    public void deleteRoute(String tenantId, UUID id) {
        Route route = routeRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        routeRepository.delete(route);
    }
}
