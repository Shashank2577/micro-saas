package com.microsaas.nexushub.workflow;

import com.microsaas.nexushub.event.EcosystemEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkflowExecutor {

    private final WorkflowRepository workflowRepository;
    private final WorkflowRunRepository runRepository;

    public List<Workflow> findMatchingWorkflows(UUID tenantId, EcosystemEvent event) {
        return workflowRepository.findByTenantIdAndEnabled(tenantId, true)
                .stream()
                .filter(w -> matchesTrigger(w.getTriggerCondition(), event))
                .toList();
    }

    @Transactional
    public WorkflowRun execute(Workflow workflow, EcosystemEvent triggerEvent) {
        WorkflowRun run = new WorkflowRun();
        run.setWorkflow(workflow);
        run.setStatus(WorkflowRun.RunStatus.RUNNING);
        runRepository.save(run);

        try {
            Map<String, Object> results = executeSteps(workflow.getSteps(), triggerEvent);
            run.setStepResults(results);
            run.setStatus(WorkflowRun.RunStatus.COMPLETED);
            run.setCompletedAt(Instant.now());
        } catch (Exception e) {
            log.error("Workflow {} failed: {}", workflow.getId(), e.getMessage(), e);
            run.setStatus(WorkflowRun.RunStatus.FAILED);
            run.setErrorMessage(e.getMessage());
            run.setCompletedAt(Instant.now());
        }

        workflow.setLastRunAt(Instant.now());
        workflowRepository.save(workflow);
        return runRepository.save(run);
    }

    private boolean matchesTrigger(Map<String, Object> trigger, EcosystemEvent event) {
        if (!"event".equals(trigger.get("type"))) {
            return false;
        }
        String requiredEventType = (String) trigger.get("eventType");
        if (requiredEventType == null) return false;

        if (requiredEventType.endsWith(".*")) {
            String prefix = requiredEventType.substring(0, requiredEventType.length() - 2);
            return event.getEventType().startsWith(prefix + ".");
        }
        return requiredEventType.equals(event.getEventType());
    }

    private Map<String, Object> executeSteps(List<Map<String, Object>> steps, EcosystemEvent triggerEvent) {
        // Step execution is intentionally simple in v1.
        // Each step is logged; capability calls will be added in Cluster I (IntegrationMesh).
        steps.forEach(step -> log.info("Executing step type={} for trigger event={}",
                step.get("type"), triggerEvent.getEventType()));
        return Map.of("stepsExecuted", steps.size(), "triggeredBy", triggerEvent.getEventType());
    }
}
