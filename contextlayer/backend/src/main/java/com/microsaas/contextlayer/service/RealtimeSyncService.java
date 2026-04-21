package com.microsaas.contextlayer.service;

import com.crosscutting.starter.queue.QueueService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealtimeSyncService {
    private final QueueService queueService;

    public void publishContextUpdate(CustomerContext context) {
        String payload = String.format("{\"customerId\":\"%s\",\"tenantId\":\"%s\",\"updatedAt\":\"%s\"}",
            context.getCustomerId(), context.getTenantId(), context.getLastUpdatedAt());
        queueService.enqueue("contextlayer.context.updated", payload, 0);
    }
}
