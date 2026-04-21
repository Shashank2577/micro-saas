package com.microsaas.usageintelligence.controller;

import com.microsaas.usageintelligence.service.UsageEventConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/usage-event-consumer")
@RequiredArgsConstructor
public class UsageEventConsumerController {

    private final UsageEventConsumer usageEventConsumer;

    @PostMapping("/handle-user-events")
    public ResponseEntity<Void> handleUserEvents(@RequestBody Map<String, Object> eventData) {
        usageEventConsumer.handleUserEvents(eventData);
        return ResponseEntity.ok().build();
    }
}
