package com.microsaas.webhookbus.service;

import com.microsaas.webhookbus.entity.DeliveryStatus;
import com.microsaas.webhookbus.entity.WebhookDelivery;
import com.microsaas.webhookbus.repository.WebhookDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);
    private static final int MAX_ATTEMPTS = 5;

    private final WebhookDeliveryRepository deliveryRepository;
    private final RestTemplate restTemplate;

    public DeliveryService(WebhookDeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
        this.restTemplate = new RestTemplate();
    }

    @Async
    public void attemptDelivery(WebhookDelivery delivery) {
        delivery.setAttemptCount(delivery.getAttemptCount() + 1);
        delivery.setLastAttemptAt(ZonedDateTime.now());
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            
            String payload = delivery.getEvent().getPayload();
            String secret = delivery.getEndpoint().getSecret();
            headers.set("X-Webhook-Signature", generateSignature(payload, secret));
            
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    delivery.getEndpoint().getUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            
            delivery.setStatusCode(response.getStatusCodeValue());
            delivery.setResponseBody(response.getBody());
            
            if (response.getStatusCode().is2xxSuccessful()) {
                delivery.setStatus(DeliveryStatus.SUCCESS);
                delivery.setNextAttemptAt(null);
            } else {
                handleFailure(delivery);
            }
        } catch (Exception e) {
            log.error("Failed to deliver webhook", e);
            delivery.setStatusCode(null);
            delivery.setResponseBody(e.getMessage());
            handleFailure(delivery);
        }
        
        deliveryRepository.save(delivery);
    }

    private void handleFailure(WebhookDelivery delivery) {
        if (delivery.getAttemptCount() >= MAX_ATTEMPTS) {
            delivery.setStatus(DeliveryStatus.FAILED);
            delivery.setNextAttemptAt(null);
        } else {
            delivery.setStatus(DeliveryStatus.PENDING);
            // Exponential backoff: 2^attempts minutes
            int delayMinutes = (int) Math.pow(2, delivery.getAttemptCount());
            delivery.setNextAttemptAt(ZonedDateTime.now().plusMinutes(delayMinutes));
        }
    }

    private String generateSignature(String payload, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return "sha256=" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            log.error("Failed to generate signature", e);
            return "";
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void retryPendingDeliveries() {
        List<WebhookDelivery> deliveries = deliveryRepository.findByStatusAndNextAttemptAtBefore(
                DeliveryStatus.PENDING, ZonedDateTime.now());
        for (WebhookDelivery delivery : deliveries) {
            attemptDelivery(delivery);
        }
    }
}
