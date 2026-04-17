package com.microsaas.experimentengine.service;

import com.microsaas.experimentengine.domain.model.Event;
import com.microsaas.experimentengine.domain.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventTrackingService {

    private final EventRepository eventRepository;

    public EventTrackingService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void trackEvents(List<Event> events) {
        eventRepository.saveAll(events);
    }
}
