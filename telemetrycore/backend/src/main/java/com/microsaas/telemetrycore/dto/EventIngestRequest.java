package com.microsaas.telemetrycore.dto;

import java.util.List;

public class EventIngestRequest {
    private List<EventDto> events;

    public List<EventDto> getEvents() { return events; }
    public void setEvents(List<EventDto> events) { this.events = events; }
}
