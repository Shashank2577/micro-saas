package com.crosscutting.starter.webhooks;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class WebhookEntityTest {

    @Test
    void webhookEndpointDefaultValues() {
        WebhookEndpoint ep = new WebhookEndpoint();
        assertThat(ep.isActive()).isTrue();
        assertThat(ep.getId()).isNull();
    }

    @Test
    void webhookEndpointAllFieldsSettable() {
        WebhookEndpoint ep = new WebhookEndpoint();
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        ep.setId(id);
        ep.setTenantId(tenantId);
        ep.setUrl("https://example.com/hook");
        ep.setSecret("my-secret");
        ep.setEventTypes("order.created,order.updated");
        ep.setActive(false);

        assertThat(ep.getId()).isEqualTo(id);
        assertThat(ep.getTenantId()).isEqualTo(tenantId);
        assertThat(ep.getUrl()).isEqualTo("https://example.com/hook");
        assertThat(ep.getSecret()).isEqualTo("my-secret");
        assertThat(ep.getEventTypes()).isEqualTo("order.created,order.updated");
        assertThat(ep.isActive()).isFalse();
    }

    @Test
    void webhookDeliveryDefaultValues() {
        WebhookDelivery d = new WebhookDelivery();
        assertThat(d.getAttemptCount()).isEqualTo(0);
        assertThat(d.getStatus()).isEqualTo("pending");
    }

    @Test
    void webhookDeliveryAllFieldsSettable() {
        WebhookDelivery d = new WebhookDelivery();
        UUID id = UUID.randomUUID();
        UUID endpointId = UUID.randomUUID();
        Instant now = Instant.now();

        d.setId(id);
        d.setEndpointId(endpointId);
        d.setEventType("order.created");
        d.setPayload("{\"test\":true}");
        d.setResponseStatus(200);
        d.setResponseBody("OK");
        d.setAttemptCount(2);
        d.setNextRetryAt(now);
        d.setStatus("delivered");

        assertThat(d.getId()).isEqualTo(id);
        assertThat(d.getEndpointId()).isEqualTo(endpointId);
        assertThat(d.getEventType()).isEqualTo("order.created");
        assertThat(d.getPayload()).isEqualTo("{\"test\":true}");
        assertThat(d.getResponseStatus()).isEqualTo(200);
        assertThat(d.getResponseBody()).isEqualTo("OK");
        assertThat(d.getAttemptCount()).isEqualTo(2);
        assertThat(d.getNextRetryAt()).isEqualTo(now);
        assertThat(d.getStatus()).isEqualTo("delivered");
    }
}
