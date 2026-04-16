package com.crosscutting.starter.webhooks;

import com.crosscutting.starter.error.CcErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final int DELIVERY_TIMEOUT_SECONDS = 10;

    private final WebhookEndpointRepository endpointRepository;
    private final WebhookDeliveryRepository deliveryRepository;
    private final HttpClient httpClient;

    public WebhookService(WebhookEndpointRepository endpointRepository,
                          WebhookDeliveryRepository deliveryRepository) {
        this(endpointRepository, deliveryRepository,
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(DELIVERY_TIMEOUT_SECONDS))
                        .build());
    }

    WebhookService(WebhookEndpointRepository endpointRepository,
                   WebhookDeliveryRepository deliveryRepository,
                   HttpClient httpClient) {
        this.endpointRepository = endpointRepository;
        this.deliveryRepository = deliveryRepository;
        this.httpClient = httpClient;
    }

    /**
     * Dispatch a webhook event to all subscribed endpoints for the given tenant.
     * Creates a delivery record for each matching endpoint.
     */
    @Transactional
    public List<WebhookDelivery> dispatch(UUID tenantId, String eventType, String payload) {
        List<WebhookEndpoint> endpoints = endpointRepository
                .findByTenantIdAndEventType(tenantId, eventType);

        List<WebhookDelivery> deliveries = endpoints.stream()
                .filter(WebhookEndpoint::isActive)
                .map(endpoint -> {
                    WebhookDelivery delivery = new WebhookDelivery();
                    delivery.setEndpointId(endpoint.getId());
                    delivery.setEventType(eventType);
                    delivery.setPayload(payload);
                    delivery.setStatus("pending");
                    delivery.setAttemptCount(0);
                    delivery = deliveryRepository.save(delivery);

                    // Actually send the webhook HTTP request
                    executeDelivery(delivery, endpoint);

                    return delivery;
                })
                .toList();

        log.info("Dispatched event '{}' to {} endpoints for tenant {}",
                eventType, deliveries.size(), tenantId);
        return deliveries;
    }

    /**
     * Execute the HTTP delivery for a webhook: POST the payload to the endpoint URL
     * with an HMAC signature header, then record the result.
     */
    private void executeDelivery(WebhookDelivery delivery, WebhookEndpoint endpoint) {
        try {
            String signature = sign(endpoint.getSecret(), delivery.getPayload());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint.getUrl()))
                    .timeout(Duration.ofSeconds(DELIVERY_TIMEOUT_SECONDS))
                    .header("Content-Type", "application/json")
                    .header("X-Webhook-Signature", "sha256=" + signature)
                    .header("X-Webhook-Event", delivery.getEventType())
                    .POST(HttpRequest.BodyPublishers.ofString(delivery.getPayload()))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            delivery.setResponseStatus(response.statusCode());
            delivery.setResponseBody(truncate(response.body(), 4000));
            delivery.setAttemptCount(delivery.getAttemptCount() + 1);

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                delivery.setStatus("delivered");
                log.info("Webhook delivery {} succeeded (status={})", delivery.getId(), response.statusCode());
            } else {
                delivery.setStatus("failed");
                delivery.setNextRetryAt(Instant.now().plusSeconds(60));
                log.warn("Webhook delivery {} failed (status={})", delivery.getId(), response.statusCode());
            }
        } catch (Exception e) {
            delivery.setAttemptCount(delivery.getAttemptCount() + 1);
            delivery.setStatus("failed");
            delivery.setNextRetryAt(Instant.now().plusSeconds(60));
            log.warn("Webhook delivery {} failed with exception: {}", delivery.getId(), e.getMessage());
        }
        deliveryRepository.save(delivery);
    }

    private static String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    /**
     * Register a new webhook endpoint for a tenant.
     */
    @Transactional
    public WebhookEndpoint registerEndpoint(UUID tenantId, String url, String secret, String eventTypes) {
        WebhookEndpoint endpoint = new WebhookEndpoint();
        endpoint.setTenantId(tenantId);
        endpoint.setUrl(url);
        endpoint.setSecret(secret);
        endpoint.setEventTypes(eventTypes);
        endpoint.setActive(true);
        endpoint = endpointRepository.save(endpoint);

        log.info("Registered webhook endpoint {} for tenant {} (url={})", endpoint.getId(), tenantId, url);
        return endpoint;
    }

    /**
     * List all active endpoints for a tenant.
     */
    public List<WebhookEndpoint> listEndpoints(UUID tenantId) {
        return endpointRepository.findByTenantIdAndActiveTrue(tenantId);
    }

    /**
     * Remove (delete) a webhook endpoint by ID.
     */
    @Transactional
    public void removeEndpoint(UUID id) {
        WebhookEndpoint endpoint = endpointRepository.findById(id)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Webhook endpoint not found: " + id));
        endpointRepository.delete(endpoint);
        log.info("Removed webhook endpoint {}", id);
    }

    /**
     * Get paginated delivery log for an endpoint.
     */
    public Page<WebhookDelivery> getDeliveries(UUID endpointId, Pageable pageable) {
        return deliveryRepository.findByEndpointId(endpointId, pageable);
    }

    /**
     * Retry a specific delivery by resetting its status.
     */
    @Transactional
    public WebhookDelivery retryDelivery(UUID deliveryId) {
        WebhookDelivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Webhook delivery not found: " + deliveryId));

        delivery.setStatus("pending");
        delivery.setAttemptCount(delivery.getAttemptCount() + 1);
        delivery.setNextRetryAt(null);
        delivery = deliveryRepository.save(delivery);

        log.info("Retrying webhook delivery {} (attempt {})", deliveryId, delivery.getAttemptCount());
        return delivery;
    }

    /**
     * Compute HMAC-SHA256 hex signature for a payload using the endpoint secret.
     */
    public String sign(String secret, String payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(keySpec);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw CcErrorCodes.internalError("Failed to compute HMAC signature", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
