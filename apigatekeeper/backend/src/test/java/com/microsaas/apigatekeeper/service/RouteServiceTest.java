package com.microsaas.apigatekeeper.service;

import com.microsaas.apigatekeeper.entity.Route;
import com.microsaas.apigatekeeper.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRoute_ShouldReturnCreatedRoute() {
        Route route = new Route();
        route.setRouteId("test-route");
        route.setPath("/api/test");

        when(routeRepository.save(any(Route.class))).thenReturn(route);

        Route created = routeService.createRoute("tenant-1", route);

        assertNotNull(created);
        assertEquals("tenant-1", route.getTenantId());
        verify(routeRepository, times(1)).save(route);
    }

    @Test
    void getRoutes_ShouldReturnList() {
        Route route = new Route();
        when(routeRepository.findByTenantId("tenant-1")).thenReturn(List.of(route));

        List<Route> routes = routeService.getRoutes("tenant-1");

        assertFalse(routes.isEmpty());
        assertEquals(1, routes.size());
    }

    @Test
    void getRoute_ShouldReturnOptionalRoute() {
        UUID id = UUID.randomUUID();
        Route route = new Route();
        when(routeRepository.findByIdAndTenantId(id, "tenant-1")).thenReturn(Optional.of(route));

        Optional<Route> found = routeService.getRoute("tenant-1", id);

        assertTrue(found.isPresent());
    }
}
