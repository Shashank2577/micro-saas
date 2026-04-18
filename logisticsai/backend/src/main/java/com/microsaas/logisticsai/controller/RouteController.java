package com.microsaas.logisticsai.controller;

import com.microsaas.logisticsai.domain.Route;
import com.microsaas.logisticsai.service.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService service;

    public RouteController(RouteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Route> getAllRoutes() {
        return service.getAllRoutes();
    }

    @PostMapping
    public Route createRoute(@RequestBody Route route) {
        return service.createRoute(route);
    }

    @PostMapping("/{id}/optimize")
    public Route optimizeRoute(@PathVariable UUID id) {
        return service.optimizeRoute(id);
    }
}
