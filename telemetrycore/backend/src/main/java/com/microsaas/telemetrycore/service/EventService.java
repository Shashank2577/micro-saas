package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.dto.EventDto;
import com.microsaas.telemetrycore.model.Event;
import com.microsaas.telemetrycore.repository.EventRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public void ingestEvents(List<EventDto> eventDtos) {
        List<Event> events = eventDtos.stream().map(dto -> {
            Event event = new Event();
            event.setTenantId(TenantContext.require());
            event.setEventName(dto.getEventName());
            event.setUserId(dto.getUserId());
            event.setSessionId(dto.getSessionId());
            event.setProperties(dto.getProperties());
            event.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : ZonedDateTime.now());
            return event;
        }).collect(Collectors.toList());
        eventRepository.saveAll(events);
    }
    
    @Transactional(readOnly = true)
    public List<Event> getSessionEvents(String sessionId) {
        return eventRepository.findByTenantIdAndSessionIdOrderByTimestampAsc(TenantContext.require(), sessionId);
    }
    
    @Transactional
    public void deleteUserData(String userId) {
        eventRepository.deleteByTenantIdAndUserId(TenantContext.require(), userId);
    }

    @Transactional(readOnly = true)
    public List<Event> exportEvents(ZonedDateTime startDate, ZonedDateTime endDate) {
        return eventRepository.findByTenantIdAndTimestampBetween(TenantContext.require(), startDate, endDate);
    }
}
