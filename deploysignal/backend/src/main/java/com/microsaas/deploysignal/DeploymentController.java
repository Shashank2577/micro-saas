package com.microsaas.deploysignal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deployments")
@RequiredArgsConstructor
public class DeploymentController {

    private final DeploymentRepository deploymentRepository;

    @GetMapping
    public List<Deployment> getAllDeployments(@RequestParam(required = false) UUID tenantId) {
        if (tenantId != null) {
            return deploymentRepository.findByTenantId(tenantId);
        }
        return deploymentRepository.findAll();
    }

    @PostMapping
    public Deployment createDeployment(@RequestBody Deployment deployment) {
        return deploymentRepository.save(deployment);
    }
}
