package com.microsaas.webhookbus.controller;

import com.microsaas.webhookbus.service.IngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ingest")
public class IngestController {

    private final IngestionService ingestionService;

    public IngestController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<Void> ingest(
            @RequestHeader(value = "X-Webhook-Source", required = false) String source,
            @RequestHeader(value = "X-Webhook-Event-Type", required = false) String eventType,
            @RequestBody String payload) {
        ingestionService.ingest(source, eventType, payload);
        return ResponseEntity.accepted().build();
    }
}
