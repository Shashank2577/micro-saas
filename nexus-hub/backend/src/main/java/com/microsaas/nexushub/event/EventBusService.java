package com.microsaas.nexushub.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventBusService {

    private final EcosystemEventRepository eventRepository;
    private final EventSubscriptionRepository subscriptionRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public EcosystemEvent publish(UUID tenantId, PublishEventRequest request) {
        EcosystemEvent event = new EcosystemEvent();
        event.setTenantId(tenantId);
        event.setSourceApp(request.sourceApp());
        event.setEventType(request.eventType());
        event.setPayload(request.payload());
        EcosystemEvent saved = eventRepository.save(event);

        fanOutAsync(tenantId, saved);
        return saved;
    }

    @Async
    protected void fanOutAsync(UUID tenantId, EcosystemEvent event) {
        List<EventSubscription> subscribers = subscriptionRepository.findByTenantId(tenantId);
        subscribers.stream()
                .filter(s -> s.matches(event.getEventType()))
                .forEach(s -> deliverToSubscriber(s, event));
    }

    private void deliverToSubscriber(EventSubscription subscription, EcosystemEvent event) {
        try {
            restTemplate.postForEntity(subscription.getCallbackUrl(), event, Void.class);
        } catch (Exception e) {
            log.warn("Failed to deliver event {} to {}: {}",
                    event.getEventType(), subscription.getSubscriberApp(), e.getMessage());
        }
    }

    @Transactional
    public EventSubscription subscribe(UUID tenantId, SubscribeRequest request) {
        EventSubscription sub = new EventSubscription();
        sub.setTenantId(tenantId);
        sub.setSubscriberApp(request.subscriberApp());
        sub.setEventTypePattern(request.eventTypePattern());
        sub.setCallbackUrl(request.callbackUrl());
        sub.setSecret(request.secret());
        return subscriptionRepository.save(sub);
    }

    public List<EcosystemEvent> listEvents(UUID tenantId, String eventType, int limit) {
        List<EcosystemEvent> events = eventType != null
                ? eventRepository.findByTenantIdAndEventTypeOrderByCreatedAtDesc(tenantId, eventType)
                : eventRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        return events.stream().limit(limit).toList();
    }
}
