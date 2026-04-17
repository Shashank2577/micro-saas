package com.crosscutting.starter.webhooks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebhookController.class)
@AutoConfigureMockMvc(addFilters = false)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebhookService webhookService;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void registerEndpoint_createsAndReturnsEndpoint() throws Exception {
        WebhookEndpoint ep = new WebhookEndpoint();
        ep.setId(UUID.randomUUID());
        ep.setTenantId(tenantId);
        ep.setUrl("https://example.com/hook");
        ep.setSecret("s3cret");
        ep.setEventTypes("order.created");
        ep.setActive(true);

        when(webhookService.registerEndpoint(tenantId, "https://example.com/hook", "s3cret", "order.created"))
                .thenReturn(ep);

        mockMvc.perform(post("/cc/webhooks/endpoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tenantId":"%s","url":"https://example.com/hook","secret":"s3cret","eventTypes":"order.created"}
                                """.formatted(tenantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://example.com/hook"))
                .andExpect(jsonPath("$.eventTypes").value("order.created"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void listEndpoints_returnsEndpointsForTenant() throws Exception {
        WebhookEndpoint ep = new WebhookEndpoint();
        ep.setId(UUID.randomUUID());
        ep.setTenantId(tenantId);
        ep.setUrl("https://example.com/hook");
        ep.setEventTypes("order.created");
        ep.setActive(true);

        when(webhookService.listEndpoints(tenantId)).thenReturn(List.of(ep));

        mockMvc.perform(get("/cc/webhooks/endpoints")
                        .param("tenantId", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("https://example.com/hook"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void removeEndpoint_returnsRemovedStatus() throws Exception {
        UUID endpointId = UUID.randomUUID();

        mockMvc.perform(delete("/cc/webhooks/endpoints/" + endpointId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("removed"));

        verify(webhookService).removeEndpoint(endpointId);
    }

    @Test
    void getDeliveries_returnsPaginatedDeliveries() throws Exception {
        UUID endpointId = UUID.randomUUID();

        WebhookDelivery delivery = new WebhookDelivery();
        delivery.setId(UUID.randomUUID());
        delivery.setEndpointId(endpointId);
        delivery.setEventType("order.created");
        delivery.setPayload("{}");
        delivery.setStatus("pending");
        delivery.setAttemptCount(0);

        when(webhookService.getDeliveries(eq(endpointId), any()))
                .thenReturn(new PageImpl<>(List.of(delivery)));

        mockMvc.perform(get("/cc/webhooks/deliveries")
                        .param("endpointId", endpointId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventType").value("order.created"))
                .andExpect(jsonPath("$.content[0].status").value("pending"));
    }

    @Test
    void retryDelivery_returnsUpdatedDelivery() throws Exception {
        UUID deliveryId = UUID.randomUUID();

        WebhookDelivery delivery = new WebhookDelivery();
        delivery.setId(deliveryId);
        delivery.setEndpointId(UUID.randomUUID());
        delivery.setEventType("order.created");
        delivery.setPayload("{}");
        delivery.setStatus("pending");
        delivery.setAttemptCount(3);

        when(webhookService.retryDelivery(deliveryId)).thenReturn(delivery);

        mockMvc.perform(post("/cc/webhooks/deliveries/" + deliveryId + "/retry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("pending"))
                .andExpect(jsonPath("$.attemptCount").value(3));
    }
}
