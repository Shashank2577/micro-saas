package com.microsaas.usageintelligence.controller;

import com.microsaas.usageintelligence.domain.AiInsight;
import com.microsaas.usageintelligence.domain.Cohort;
import com.microsaas.usageintelligence.domain.Event;
import com.microsaas.usageintelligence.domain.Metric;
import com.microsaas.usageintelligence.dto.CreateCohortDto;
import com.microsaas.usageintelligence.dto.CreateEventDto;
import com.microsaas.usageintelligence.service.UsageIntelligenceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UsageIntelligenceController {

    private final UsageIntelligenceService service;

    public UsageIntelligenceController(UsageIntelligenceService service) {
        this.service = service;
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event trackEvent(@RequestBody CreateEventDto dto) {
        return service.trackEvent(dto);
    }

    @GetMapping("/events")
    public List<Event> getEvents() {
        return service.getRecentEvents();
    }

    @GetMapping("/metrics")
    public List<Metric> getMetrics(
            @RequestParam String metricName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return service.getMetrics(metricName, startDate, endDate);
    }

    @GetMapping("/cohorts")
    public List<Cohort> getCohorts() {
        return service.getCohorts();
    }

    @PostMapping("/cohorts")
    @ResponseStatus(HttpStatus.CREATED)
    public Cohort createCohort(@RequestBody CreateCohortDto dto) {
        return service.createCohort(dto);
    }

    @GetMapping("/insights")
    public List<AiInsight> getInsights() {
        return service.getInsights();
    }

    @PostMapping("/insights/generate")
    public AiInsight generateInsight() {
        return service.generateInsight();
    }
}
