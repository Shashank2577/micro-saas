package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.dto.EventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
public class KafkaEventConsumer {

    @Autowired
    private EventService eventService;

    @EventListener
    public void handleIntegrationEvent(Map<String, Object> eventPayload) {
        if (!eventPayload.containsKey("eventName")) return;
        
        String eventName = (String) eventPayload.get("eventName");
        if (List.of("page_view", "user_signup", "feature_adopted").contains(eventName)) {
            EventDto dto = new EventDto();
            dto.setEventName(eventName);
            dto.setUserId((String) eventPayload.get("userId"));
            dto.setTimestamp(ZonedDateTime.now());
            dto.setProperties(eventPayload);
            eventService.ingestEvents(List.of(dto));
        }
    }
}
