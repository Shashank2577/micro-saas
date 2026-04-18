package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Event;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class MetricAggregator {
    
    public Map<String, Object> aggregateEvents(List<Event> events, String aggregationType) {
        Map<String, Object> result = new HashMap<>();
        
        if (events == null || events.isEmpty()) {
            result.put("value", 0);
            return result;
        }

        switch (aggregationType.toUpperCase()) {
            case "COUNT":
                result.put("value", events.size());
                break;
            case "UNIQUE_USERS":
                long uniqueUsers = events.stream()
                        .map(Event::getUserId)
                        .filter(id -> id != null)
                        .distinct()
                        .count();
                result.put("value", uniqueUsers);
                break;
            default:
                result.put("value", events.size());
        }
        
        return result;
    }
}
