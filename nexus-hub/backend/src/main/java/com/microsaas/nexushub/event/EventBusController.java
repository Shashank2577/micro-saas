package com.microsaas.nexushub.event;

import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventBusController {

    private final EventBusService eventBusService;

    @PostMapping
    public ResponseEntity<EcosystemEvent> publish(@RequestBody PublishEventRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(eventBusService.publish(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<EcosystemEvent>> list(
            @RequestParam(required = false) String eventType,
            @RequestParam(defaultValue = "100") int limit) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(eventBusService.listEvents(tenantId, eventType, limit));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<EventSubscription> subscribe(@RequestBody SubscribeRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(eventBusService.subscribe(tenantId, request));
    }
}
