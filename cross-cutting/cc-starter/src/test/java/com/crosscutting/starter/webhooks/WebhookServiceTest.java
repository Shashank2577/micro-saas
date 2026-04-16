package com.crosscutting.starter.webhooks;

import com.crosscutting.starter.error.CcException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookServiceTest {

    @Mock
    private WebhookEndpointRepository endpointRepository;

    @Mock
    private WebhookDeliveryRepository deliveryRepository;

    @Mock
    private HttpClient httpClient;

    @SuppressWarnings("unchecked")
    @Mock
    private HttpResponse<String> httpResponse;

    private WebhookService webhookService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        webhookService = new WebhookService(endpointRepository, deliveryRepository, httpClient);
    }

    @Test
    void dispatch_createsDeliveriesForSubscribedEndpoints() throws Exception {
        UUID endpointId1 = UUID.randomUUID();
        UUID endpointId2 = UUID.randomUUID();

        WebhookEndpoint ep1 = new WebhookEndpoint();
        ep1.setId(endpointId1);
        ep1.setTenantId(tenantId);
        ep1.setActive(true);
        ep1.setUrl("https://example.com/hook1");
        ep1.setSecret("secret1");
        ep1.setEventTypes("order.created,order.updated");

        WebhookEndpoint ep2 = new WebhookEndpoint();
        ep2.setId(endpointId2);
        ep2.setTenantId(tenantId);
        ep2.setActive(true);
        ep2.setUrl("https://example.com/hook2");
        ep2.setSecret("secret2");
        ep2.setEventTypes("order.created");

        when(endpointRepository.findByTenantIdAndEventType(tenantId, "order.created"))
                .thenReturn(List.of(ep1, ep2));

        when(deliveryRepository.save(any(WebhookDelivery.class)))
                .thenAnswer(invocation -> {
                    WebhookDelivery d = invocation.getArgument(0);
                    if (d.getId() == null) d.setId(UUID.randomUUID());
                    return d;
                });

        // Mock successful HTTP delivery
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("OK");
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        List<WebhookDelivery> deliveries = webhookService.dispatch(tenantId, "order.created", "{\"orderId\":1}");

        assertThat(deliveries).hasSize(2);
        assertThat(deliveries).allSatisfy(d -> {
            assertThat(d.getEventType()).isEqualTo("order.created");
            assertThat(d.getPayload()).isEqualTo("{\"orderId\":1}");
            assertThat(d.getStatus()).isEqualTo("delivered");
            assertThat(d.getAttemptCount()).isEqualTo(1);
            assertThat(d.getResponseStatus()).isEqualTo(200);
        });

        verify(httpClient, times(2)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void dispatch_marksDeliveryAsFailedOnHttpError() throws Exception {
        WebhookEndpoint ep = new WebhookEndpoint();
        ep.setId(UUID.randomUUID());
        ep.setTenantId(tenantId);
        ep.setActive(true);
        ep.setUrl("https://example.com/hook");
        ep.setSecret("secret");
        ep.setEventTypes("order.created");

        when(endpointRepository.findByTenantIdAndEventType(tenantId, "order.created"))
                .thenReturn(List.of(ep));

        when(deliveryRepository.save(any(WebhookDelivery.class)))
                .thenAnswer(invocation -> {
                    WebhookDelivery d = invocation.getArgument(0);
                    if (d.getId() == null) d.setId(UUID.randomUUID());
                    return d;
                });

        when(httpResponse.statusCode()).thenReturn(500);
        when(httpResponse.body()).thenReturn("Internal Server Error");
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        List<WebhookDelivery> deliveries = webhookService.dispatch(tenantId, "order.created", "{}");

        assertThat(deliveries).hasSize(1);
        assertThat(deliveries.get(0).getStatus()).isEqualTo("failed");
        assertThat(deliveries.get(0).getResponseStatus()).isEqualTo(500);
        assertThat(deliveries.get(0).getNextRetryAt()).isNotNull();
    }

    @Test
    void dispatch_marksDeliveryAsFailedOnConnectionException() throws Exception {
        WebhookEndpoint ep = new WebhookEndpoint();
        ep.setId(UUID.randomUUID());
        ep.setTenantId(tenantId);
        ep.setActive(true);
        ep.setUrl("https://example.com/hook");
        ep.setSecret("secret");
        ep.setEventTypes("order.created");

        when(endpointRepository.findByTenantIdAndEventType(tenantId, "order.created"))
                .thenReturn(List.of(ep));

        when(deliveryRepository.save(any(WebhookDelivery.class)))
                .thenAnswer(invocation -> {
                    WebhookDelivery d = invocation.getArgument(0);
                    if (d.getId() == null) d.setId(UUID.randomUUID());
                    return d;
                });

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new java.io.IOException("Connection refused"));

        List<WebhookDelivery> deliveries = webhookService.dispatch(tenantId, "order.created", "{}");

        assertThat(deliveries).hasSize(1);
        assertThat(deliveries.get(0).getStatus()).isEqualTo("failed");
        assertThat(deliveries.get(0).getAttemptCount()).isEqualTo(1);
        assertThat(deliveries.get(0).getNextRetryAt()).isNotNull();
    }

    @Test
    void dispatch_skipsInactiveEndpoints() {
        WebhookEndpoint inactive = new WebhookEndpoint();
        inactive.setId(UUID.randomUUID());
        inactive.setTenantId(tenantId);
        inactive.setActive(false);
        inactive.setEventTypes("order.created");

        when(endpointRepository.findByTenantIdAndEventType(tenantId, "order.created"))
                .thenReturn(List.of(inactive));

        List<WebhookDelivery> deliveries = webhookService.dispatch(tenantId, "order.created", "{}");

        assertThat(deliveries).isEmpty();
    }

    @Test
    void sign_producesValidHmacSha256Hex() {
        String secret = "test-secret";
        String payload = "hello world";

        String signature = webhookService.sign(secret, payload);

        assertThat(signature).isNotBlank();
        assertThat(signature).matches("[0-9a-f]{64}");

        // Same input produces same signature (deterministic)
        String signature2 = webhookService.sign(secret, payload);
        assertThat(signature).isEqualTo(signature2);

        // Different secret produces different signature
        String differentSig = webhookService.sign("other-secret", payload);
        assertThat(differentSig).isNotEqualTo(signature);
    }

    @Test
    void registerEndpoint_savesAndReturnsEndpoint() {
        when(endpointRepository.save(any(WebhookEndpoint.class)))
                .thenAnswer(invocation -> {
                    WebhookEndpoint ep = invocation.getArgument(0);
                    ep.setId(UUID.randomUUID());
                    return ep;
                });

        WebhookEndpoint result = webhookService.registerEndpoint(
                tenantId, "https://example.com/hook", "my-secret", "order.created,order.updated");

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTenantId()).isEqualTo(tenantId);
        assertThat(result.getUrl()).isEqualTo("https://example.com/hook");
        assertThat(result.getSecret()).isEqualTo("my-secret");
        assertThat(result.getEventTypes()).isEqualTo("order.created,order.updated");
        assertThat(result.isActive()).isTrue();
    }

    @Test
    void listEndpoints_returnsActiveEndpointsForTenant() {
        WebhookEndpoint ep = new WebhookEndpoint();
        ep.setId(UUID.randomUUID());
        ep.setTenantId(tenantId);
        ep.setActive(true);

        when(endpointRepository.findByTenantIdAndActiveTrue(tenantId))
                .thenReturn(List.of(ep));

        List<WebhookEndpoint> result = webhookService.listEndpoints(tenantId);

        assertThat(result).hasSize(1);
    }

    @Test
    void removeEndpoint_deletesExistingEndpoint() {
        UUID endpointId = UUID.randomUUID();
        WebhookEndpoint ep = new WebhookEndpoint();
        ep.setId(endpointId);

        when(endpointRepository.findById(endpointId)).thenReturn(Optional.of(ep));

        webhookService.removeEndpoint(endpointId);

        verify(endpointRepository).delete(ep);
    }

    @Test
    void removeEndpoint_throwsWhenNotFound() {
        UUID missingId = UUID.randomUUID();
        when(endpointRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> webhookService.removeEndpoint(missingId))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "RESOURCE_NOT_FOUND");
    }

    @Test
    void getDeliveries_returnsPaginatedResults() {
        UUID endpointId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        WebhookDelivery delivery = new WebhookDelivery();
        delivery.setId(UUID.randomUUID());
        delivery.setEndpointId(endpointId);

        when(deliveryRepository.findByEndpointId(endpointId, pageable))
                .thenReturn(new PageImpl<>(List.of(delivery)));

        Page<WebhookDelivery> result = webhookService.getDeliveries(endpointId, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void retryDelivery_resetsStatusAndIncrementsAttempt() {
        UUID deliveryId = UUID.randomUUID();
        WebhookDelivery delivery = new WebhookDelivery();
        delivery.setId(deliveryId);
        delivery.setStatus("failed");
        delivery.setAttemptCount(2);

        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any(WebhookDelivery.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WebhookDelivery result = webhookService.retryDelivery(deliveryId);

        assertThat(result.getStatus()).isEqualTo("pending");
        assertThat(result.getAttemptCount()).isEqualTo(3);
        assertThat(result.getNextRetryAt()).isNull();
    }
}
