package com.microsaas.nexushub.workflow;

import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @PostMapping
    public ResponseEntity<Workflow> create(@RequestBody CreateWorkflowRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(workflowService.create(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<Workflow>> list() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(workflowService.list(tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workflow> update(@PathVariable UUID id, @RequestBody CreateWorkflowRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(workflowService.update(tenantId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        workflowService.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/runs")
    public ResponseEntity<List<WorkflowRun>> listRuns(@PathVariable UUID id) {
        return ResponseEntity.ok(workflowService.listRuns(id));
    }
}
