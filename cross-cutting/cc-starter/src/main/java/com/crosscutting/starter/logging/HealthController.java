package com.crosscutting.starter.logging;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/cc/health")
@Tag(name = "Health", description = "Application health and readiness checks")
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping
    @Operation(summary = "Liveness check", description = "Returns basic health status indicating the application is running")
    @ApiResponse(responseCode = "200", description = "Application is alive")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/ready")
    @Operation(summary = "Readiness check", description = "Checks application readiness including database connectivity")
    @ApiResponse(responseCode = "200", description = "Readiness status returned")
    @ApiResponse(responseCode = "500", description = "Internal server error during readiness check")
    public Map<String, Object> ready() {
        String dbStatus = "UP";
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(2)) {
                dbStatus = "DOWN";
            }
        } catch (Exception e) {
            log.warn("Database readiness check failed", e);
            dbStatus = "DOWN";
        }

        String overallStatus = "UP".equals(dbStatus) ? "UP" : "DOWN";

        return Map.of(
                "status", overallStatus,
                "timestamp", Instant.now().toString(),
                "checks", Map.of("database", dbStatus)
        );
    }
}
