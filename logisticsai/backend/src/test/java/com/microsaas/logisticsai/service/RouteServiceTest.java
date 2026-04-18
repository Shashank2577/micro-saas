package com.microsaas.logisticsai.service;

import com.microsaas.logisticsai.domain.Route;
import com.microsaas.logisticsai.repository.RouteRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RouteServiceTest {

    @Mock
    private RouteRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private RouteService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void getAllRoutes() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(new Route()));
        List<Route> routes = service.getAllRoutes();
        assertFalse(routes.isEmpty());
    }

    @Test
    void createRoute() {
        Route route = new Route();
        route.setOrigin("NY");
        route.setDestination("LA");
        when(repository.save(any(Route.class))).thenAnswer(invocation -> {
            Route r = invocation.getArgument(0);
            r.setId(UUID.randomUUID());
            return r;
        });

        Route saved = service.createRoute(route);
        assertEquals("NY", saved.getOrigin());
        assertEquals("PLANNED", saved.getStatus());
    }

    @Test
    void optimizeRoute() {
        UUID id = UUID.randomUUID();
        Route route = new Route();
        route.setId(id);
        route.setOrigin("NY");
        route.setDestination("LA");
        route.setStatus("PLANNED");
        
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(route));
        when(repository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Route optimized = service.optimizeRoute(id);
        assertEquals("OPTIMIZED", optimized.getStatus());
        assertNotNull(optimized.getEstimatedArrival());
        
        verify(eventPublisher).publishEvent(eq("route.optimized"), anyMap());
    }
}
