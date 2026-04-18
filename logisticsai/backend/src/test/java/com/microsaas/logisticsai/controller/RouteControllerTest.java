package com.microsaas.logisticsai.controller;

import com.microsaas.logisticsai.domain.Route;
import com.microsaas.logisticsai.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteControllerTest {

    @Mock
    private RouteService service;

    @InjectMocks
    private RouteController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRoutes() {
        when(service.getAllRoutes()).thenReturn(List.of(new Route()));
        List<Route> routes = controller.getAllRoutes();
        assertFalse(routes.isEmpty());
    }

    @Test
    void createRoute() {
        Route route = new Route();
        when(service.createRoute(route)).thenReturn(route);
        Route response = controller.createRoute(route);
        assertNotNull(response);
    }

    @Test
    void optimizeRoute() {
        UUID id = UUID.randomUUID();
        when(service.optimizeRoute(id)).thenReturn(new Route());
        Route response = controller.optimizeRoute(id);
        assertNotNull(response);
    }
}
