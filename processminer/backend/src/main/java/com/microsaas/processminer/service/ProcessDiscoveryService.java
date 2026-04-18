package com.microsaas.processminer.service;

import com.microsaas.processminer.domain.EventLog;
import com.microsaas.processminer.domain.ProcessModel;
import com.microsaas.processminer.repository.EventLogRepository;
import com.microsaas.processminer.repository.ProcessModelRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProcessDiscoveryService {
    
    private final ProcessModelRepository processModelRepository;
    private final EventLogRepository eventLogRepository;

    public ProcessDiscoveryService(ProcessModelRepository processModelRepository, EventLogRepository eventLogRepository) {
        this.processModelRepository = processModelRepository;
        this.eventLogRepository = eventLogRepository;
    }

    @Transactional
    public ProcessModel discoverProcessModel(String systemType) {
        UUID tenantId = TenantContext.require();
        List<EventLog> events = eventLogRepository.findByTenantIdAndSystemTypeOrderByTimestampAsc(tenantId, systemType);
        
        if (events.isEmpty()) {
            throw new IllegalArgumentException("No events found for system type: " + systemType);
        }

        // Simulate PM4PY process discovery - building a mock BPMN from unique activities
        String activities = events.stream()
                .map(EventLog::getActivityName)
                .distinct()
                .collect(Collectors.joining(" -> "));

        String bpmnMock = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><bpmn:definitions><bpmn:process id=\"Process_1\"><!-- MOCK BPMN --><documentation>Activities: " + activities + "</documentation></bpmn:process></bpmn:definitions>";

        ProcessModel model = new ProcessModel();
        model.setTenantId(tenantId);
        model.setSystemType(systemType);
        model.setName("Discovered Process - " + systemType);
        model.setDescription("Automatically discovered from event logs");
        model.setBpmnXml(bpmnMock);
        
        return processModelRepository.save(model);
    }
    
    public List<ProcessModel> getModels() {
        return processModelRepository.findByTenantId(TenantContext.require());
    }
    
    public ProcessModel getModel(UUID id) {
        return processModelRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Process model not found"));
    }
}
