package com.microsaas.apigatekeeper.service;

import com.microsaas.apigatekeeper.entity.Webhook;
import com.microsaas.apigatekeeper.entity.WebhookDelivery;
import com.microsaas.apigatekeeper.repository.WebhookDeliveryRepository;
import com.microsaas.apigatekeeper.repository.WebhookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookRepository webhookRepository;
    private final WebhookDeliveryRepository deliveryRepository;

    @Transactional
    public Webhook registerWebhook(String tenantId, Webhook webhook) {
        webhook.setTenantId(tenantId);
        webhook.setCreatedAt(ZonedDateTime.now());
        webhook.setUpdatedAt(ZonedDateTime.now());
        return webhookRepository.save(webhook);
    }

    public List<Webhook> getWebhooks(String tenantId) {
        return webhookRepository.findByTenantId(tenantId);
    }

    public List<WebhookDelivery> getDeliveries(String tenantId, UUID webhookId) {
        return deliveryRepository.findByWebhookIdAndTenantId(webhookId, tenantId);
    }
}
