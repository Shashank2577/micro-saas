package com.microsaas.apigatekeeper.config;

import com.microsaas.apigatekeeper.repository.RouteRepository;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class DynamicRouteLocatorConfig {

    private final RouteRepository routeRepository;

    public DynamicRouteLocatorConfig(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Bean
    public RouterFunction<ServerResponse> customRouteLocator() {
        var builder = GatewayRouterFunctions.route("default-fallback")
                .route(path("/not-found"), HandlerFunctions.http("http://localhost:8102/"));

        routeRepository.findAll().forEach(dbRoute -> {
            // Using ID prefix logic since Spring Gateway MVC requires RequestPredicate + HandlerFunction
            // to properly establish routing definitions.
            builder.route(path(dbRoute.getPath()), HandlerFunctions.http(dbRoute.getTargetUrl()));
        });

        return builder.build();
    }
}
