package com.crosscutting.starter.webhooks;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cc/webhooks")
@Tag(name = "Webhooks", description = "Webhook endpoint registration, delivery logs, and retry")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    /**
     * Register a new webhook endpoint.
     */
    @PostMapping("/endpoints")
    @Operation(summary = "Register a webhook endpoint", description = "Register a new URL to receive webhook events for a tenant")
    @ApiResponse(responseCode = "200", description = "Endpoint registered successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public WebhookEndpoint registerEndpoint(@Valid @RequestBody RegisterEndpointRequest request) {
        return webhookService.registerEndpoint(
                request.tenantId(), request.url(), request.secret(), request.eventTypes());
    }

    /**
     * List all active endpoints for a tenant.
     */
    @GetMapping("/endpoints")
    @Operation(summary = "List webhook endpoints", description = "List all active webhook endpoints registered for a tenant")
    @ApiResponse(responseCode = "200", description = "Endpoints listed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<WebhookEndpoint> listEndpoints(@Parameter(description = "Tenant ID") @RequestParam UUID tenantId) {
        return webhookService.listEndpoints(tenantId);
    }

    /**
     * Remove a webhook endpoint.
     */
    @DeleteMapping("/endpoints/{id}")
    @Operation(summary = "Remove a webhook endpoint", description = "Deactivate and remove a webhook endpoint by its ID")
    @ApiResponse(responseCode = "200", description = "Endpoint removed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Endpoint not found")
    public Map<String, String> removeEndpoint(@Parameter(description = "Endpoint ID") @PathVariable UUID id) {
        webhookService.removeEndpoint(id);
        return Map.of("status", "removed");
    }

    /**
     * Get paginated delivery log for an endpoint.
     */
    @GetMapping("/deliveries")
    @Operation(summary = "Get delivery log", description = "Retrieve paginated delivery history for a specific webhook endpoint")
    @ApiResponse(responseCode = "200", description = "Delivery log returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Endpoint not found")
    public Page<WebhookDelivery> getDeliveries(@Parameter(description = "Endpoint ID") @RequestParam UUID endpointId, Pageable pageable) {
        return webhookService.getDeliveries(endpointId, pageable);
    }

    /**
     * Retry a failed delivery.
     */
    @PostMapping("/deliveries/{id}/retry")
    @Operation(summary = "Retry a failed delivery", description = "Re-attempt delivery for a previously failed webhook event")
    @ApiResponse(responseCode = "200", description = "Delivery retried successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Delivery not found")
    public WebhookDelivery retryDelivery(@Parameter(description = "Delivery ID") @PathVariable UUID id) {
        return webhookService.retryDelivery(id);
    }

    public record RegisterEndpointRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "URL is required") @URL(message = "Must be a valid URL") String url,
            @NotBlank(message = "Secret is required") String secret,
            @NotBlank(message = "Event types are required") String eventTypes) {
    }
}
