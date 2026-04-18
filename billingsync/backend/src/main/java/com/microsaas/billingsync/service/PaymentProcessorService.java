package com.microsaas.billingsync.service;

import com.microsaas.billingsync.model.Payment;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentProcessorService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // In a real application, this would use a Stripe SDK or HTTP client to a real payment gateway.
    // Given the constraints of this task and lack of external API keys, we simulate an intelligent processor.
    public PaymentResult processPayment(String paymentMethodId, BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(false, null, "Invalid amount. Must be greater than zero.");
        }
        
        // Simulate external API call delay
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        // Simulate failure for specific mock cards
        if (paymentMethodId.contains("fail")) {
             return new PaymentResult(false, null, "Insufficient funds or card declined.");
        }
        
        String transactionId = "txn_" + UUID.randomUUID().toString().replace("-", "");
        return new PaymentResult(true, transactionId, null);
    }
    
    public RefundResult processRefund(String transactionId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new RefundResult(false, "Invalid refund amount.");
        }
        // Simulate external API call delay
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        return new RefundResult(true, null);
    }

    public static class PaymentResult {
        public boolean success;
        public String transactionId;
        public String errorMessage;
        
        public PaymentResult(boolean success, String transactionId, String errorMessage) {
            this.success = success;
            this.transactionId = transactionId;
            this.errorMessage = errorMessage;
        }
    }
    
    public static class RefundResult {
        public boolean success;
        public String errorMessage;
        
        public RefundResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
    }
}
