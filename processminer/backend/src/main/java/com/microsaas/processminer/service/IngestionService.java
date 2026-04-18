package com.microsaas.processminer.service;

import com.microsaas.processminer.domain.EventLog;
import com.microsaas.processminer.dto.EventIngestRequest;
import com.microsaas.processminer.repository.EventLogRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class IngestionService {
    
    private final EventLogRepository eventLogRepository;
    private final ObjectMapper objectMapper;

    public IngestionService(EventLogRepository eventLogRepository, ObjectMapper objectMapper) {
        this.eventLogRepository = eventLogRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public EventLog ingestEvent(EventIngestRequest request) {
        EventLog event = new EventLog();
        event.setTenantId(TenantContext.require());
        event.setSystemType(request.systemType());
        event.setCaseId(request.caseId());
        event.setActivityName(request.activityName());
        event.setActorId(request.actorId());
        event.setTimestamp(request.timestamp());
        
        if (request.attributes() != null) {
            event.setAttributes(objectMapper.valueToTree(request.attributes()));
        }
        
        return eventLogRepository.save(event);
    }
    
    public List<EventLog> getEvents() {
        return eventLogRepository.findByTenantId(TenantContext.require());
    }
}
