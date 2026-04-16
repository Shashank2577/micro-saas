package com.crosscutting.starter.payments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/cc/payments")
@Tag(name = "Payments", description = "Payment intents, subscriptions, and webhook handling")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/intents")
    @Operation(summary = "Create a payment intent", description = "Create a new payment intent with the specified amount, currency, and metadata")
    @ApiResponse(responseCode = "200", description = "Payment intent created successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Payment provider error")
    public PaymentIntent createIntent(@Valid @RequestBody CreateIntentRequest request) {
        return paymentService.createPaymentIntent(request.amount(), request.currency(), request.metadata());
    }

    @PostMapping("/subscriptions")
    @Operation(summary = "Create a subscription", description = "Create a new recurring subscription for a customer with a specific price plan")
    @ApiResponse(responseCode = "200", description = "Subscription created successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Payment provider error")
    public Subscription createSubscription(@Valid @RequestBody CreateSubscriptionRequest request) {
        return paymentService.createSubscription(request.customerId(), request.priceId());
    }

    @DeleteMapping("/subscriptions/{id}")
    @Operation(summary = "Cancel a subscription", description = "Cancel an active subscription by its identifier")
    @ApiResponse(responseCode = "200", description = "Subscription cancelled successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Subscription not found")
    public Map<String, String> cancelSubscription(@Parameter(description = "Subscription ID") @PathVariable String id) {
        paymentService.cancelSubscription(id);
        return Map.of("status", "cancelled");
    }

    @PostMapping("/webhooks")
    @Operation(summary = "Handle payment webhook", description = "Process incoming webhook events from the payment provider")
    @ApiResponse(responseCode = "200", description = "Webhook processed successfully")
    @ApiResponse(responseCode = "500", description = "Webhook processing error")
    public Map<String, Object> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String signature) {
        if (signature == null || !paymentService.verifyWebhookSignature(payload, signature)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid webhook signature");
        }
        return paymentService.handleWebhook(payload);
    }

    public record CreateIntentRequest(
            @Positive(message = "Amount must be positive") long amount,
            @NotBlank(message = "Currency is required") String currency,
            Map<String, String> metadata) {
    }

    public record CreateSubscriptionRequest(
            @NotBlank(message = "Customer ID is required") String customerId,
            @NotBlank(message = "Price ID is required") String priceId) {
    }
}
