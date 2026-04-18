package com.microsaas.processminer.controller;

import com.microsaas.processminer.domain.EventLog;
import com.microsaas.processminer.dto.EventIngestRequest;
import com.microsaas.processminer.service.IngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final IngestionService ingestionService;

    public EventController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    @ResponseStatus(HttpStatus.CREATED)
    public EventLog ingest(@RequestBody @Valid EventIngestRequest request) {
        return ingestionService.ingestEvent(request);
    }
    
    @GetMapping
    public List<EventLog> getEvents() {
        return ingestionService.getEvents();
    }
}
