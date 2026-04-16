package com.microsaas.nexushub.workflow;

import com.microsaas.nexushub.event.EcosystemEvent;
import com.microsaas.nexushub.event.EventBusService;
import com.microsaas.nexushub.event.PublishEventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowRunRepository runRepository;
    private final WorkflowExecutor executor;
    private final EventBusService eventBusService;

    @Transactional
    public Workflow create(UUID tenantId, CreateWorkflowRequest request) {
        Workflow workflow = new Workflow();
        workflow.setTenantId(tenantId);
        workflow.setName(request.name());
        workflow.setDescription(request.description());
        workflow.setTriggerCondition(request.triggerCondition());
        workflow.setSteps(request.steps());
        workflow.setEnabled(true);
        workflow.setCreatedAt(Instant.now());
        workflow.setUpdatedAt(Instant.now());
        return workflowRepository.save(workflow);
    }

    public List<Workflow> list(UUID tenantId) {
        return workflowRepository.findByTenantIdAndEnabled(tenantId, true);
    }

    @Transactional
    public Workflow update(UUID tenantId, UUID id, CreateWorkflowRequest request) {
        return workflowRepository.findById(id)
                .filter(w -> w.getTenantId().equals(tenantId))
                .map(w -> {
                    w.setName(request.name());
                    w.setDescription(request.description());
                    w.setTriggerCondition(request.triggerCondition());
                    w.setSteps(request.steps());
                    w.setUpdatedAt(Instant.now());
                    return workflowRepository.save(w);
                })
                .orElseThrow();
    }

    @Transactional
    public void delete(UUID tenantId, UUID id) {
        workflowRepository.findById(id)
                .filter(w -> w.getTenantId().equals(tenantId))
                .ifPresent(w -> {
                    w.setEnabled(false);
                    workflowRepository.save(w);
                });
    }

    public List<WorkflowRun> listRuns(UUID workflowId) {
        return runRepository.findByWorkflowIdOrderByTriggeredAtDesc(workflowId);
    }

    public void triggerWorkflows(UUID tenantId, EcosystemEvent event) {
        List<Workflow> matched = executor.findMatchingWorkflows(tenantId, event);
        matched.forEach(w -> executor.execute(w, event));
    }
}
