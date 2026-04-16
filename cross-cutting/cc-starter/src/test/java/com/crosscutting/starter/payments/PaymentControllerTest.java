package com.crosscutting.starter.payments;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void createIntent_returnsPaymentIntent() throws Exception {
        PaymentIntent intent = new PaymentIntent("pi_123", 5000, "usd", "requires_payment_method", Map.of());
        when(paymentService.createPaymentIntent(eq(5000L), eq("usd"), any())).thenReturn(intent);

        mockMvc.perform(post("/cc/payments/intents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":5000,"currency":"usd","metadata":{}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pi_123"))
                .andExpect(jsonPath("$.amount").value(5000))
                .andExpect(jsonPath("$.currency").value("usd"));
    }

    @Test
    void createSubscription_returnsSubscription() throws Exception {
        Subscription sub = new Subscription("sub_123", "cus_456", "price_789", "active", Instant.parse("2026-04-17T00:00:00Z"));
        when(paymentService.createSubscription("cus_456", "price_789")).thenReturn(sub);

        mockMvc.perform(post("/cc/payments/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"customerId":"cus_456","priceId":"price_789"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("sub_123"))
                .andExpect(jsonPath("$.customerId").value("cus_456"))
                .andExpect(jsonPath("$.status").value("active"));
    }

    @Test
    void cancelSubscription_returnsCancelledStatus() throws Exception {
        mockMvc.perform(delete("/cc/payments/subscriptions/sub_123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("cancelled"));

        verify(paymentService).cancelSubscription("sub_123");
    }

    @Test
    void handleWebhook_returnsWebhookResult() throws Exception {
        String payload = "{\"type\":\"payment_intent.succeeded\"}";
        when(paymentService.verifyWebhookSignature(payload, "sig_test")).thenReturn(true);
        when(paymentService.handleWebhook(payload)).thenReturn(Map.of("status", "received"));

        mockMvc.perform(post("/cc/payments/webhooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", "sig_test")
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    void handleWebhook_rejectsWithoutSignature() throws Exception {
        mockMvc.perform(post("/cc/payments/webhooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"payment_intent.succeeded\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }

    @Test
    void handleWebhook_rejectsInvalidSignature() throws Exception {
        String payload = "{\"type\":\"payment_intent.succeeded\"}";
        when(paymentService.verifyWebhookSignature(payload, "bad_sig")).thenReturn(false);

        mockMvc.perform(post("/cc/payments/webhooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", "bad_sig")
                        .content(payload))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }
}
