package com.microsaas.dependencyradar.controller;

import com.microsaas.dependencyradar.dto.GithubPushEvent;
import com.microsaas.dependencyradar.service.DependencyScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final DependencyScannerService scannerService;

    @Autowired
    public WebhookController(DependencyScannerService scannerService) {
        this.scannerService = scannerService;
    }

    @PostMapping("/github")
    public ResponseEntity<String> handleGithubPush(@RequestBody GithubPushEvent event) {
        // In a real app we would verify GitHub signature
        
        if (event.getCommits() == null || event.getCommits().isEmpty()) {
            return ResponseEntity.ok("No commits found in push event");
        }
        
        // Check if any commit modified dependency files (pom.xml, package.json)
        boolean hasDependencyChanges = event.getCommits().stream()
            .anyMatch(commit -> 
                hasDependencyFile(commit.getAdded()) || 
                hasDependencyFile(commit.getModified())
            );

        if (hasDependencyChanges) {
            String branch = event.getRef().replace("refs/heads/", "");
            String owner = event.getRepository().getOwner() != null ? event.getRepository().getOwner().getLogin() : null;
            if(owner == null) owner = event.getRepository().getOwner().getName();
            
            scannerService.scanRepository(owner, event.getRepository().getName(), branch, event.getRepository().getId());
            return ResponseEntity.ok("Scan initiated");
        }
        
        return ResponseEntity.ok("No dependency changes detected");
    }

    private boolean hasDependencyFile(java.util.List<String> files) {
        if (files == null) return false;
        return files.stream().anyMatch(f -> f.endsWith("pom.xml") || f.endsWith("package.json"));
    }
}
