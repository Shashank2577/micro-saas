package com.microsaas.logisticsai.service;

import com.microsaas.logisticsai.domain.Route;
import com.microsaas.logisticsai.repository.RouteRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RouteService {

    private final RouteRepository repository;
    private final EventPublisherService eventPublisher;

    public RouteService(RouteRepository repository, EventPublisherService eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<Route> getAllRoutes() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional
    public Route createRoute(Route route) {
        route.setTenantId(TenantContext.require());
        route.setStatus("PLANNED");
        return repository.save(route);
    }

    @Transactional
    public Route optimizeRoute(UUID id) {
        Route route = repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Route not found"));
        // Simulated AI optimization logic
        route.setStatus("OPTIMIZED");
        route.setEstimatedArrival(LocalDateTime.now().plusDays(2));
        
        Route saved = repository.save(route);
        
        eventPublisher.publishEvent("route.optimized", Map.of(
            "routeId", saved.getId(),
            "origin", saved.getOrigin(),
            "destination", saved.getDestination(),
            "estimatedArrival", saved.getEstimatedArrival().toString()
        ));
        
        return saved;
    }
}
