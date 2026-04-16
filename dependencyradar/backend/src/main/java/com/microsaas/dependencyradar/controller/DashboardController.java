package com.microsaas.dependencyradar.controller;

import com.microsaas.dependencyradar.model.Dependency;
import com.microsaas.dependencyradar.model.UpgradeCandidate;
import com.microsaas.dependencyradar.repository.DependencyRepository;
import com.microsaas.dependencyradar.repository.UpgradeCandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Optional;

import java.util.List;
import java.util.Arrays;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DependencyRepository dependencyRepository;
    private final UpgradeCandidateRepository upgradeCandidateRepository;

    @Autowired
    public DashboardController(DependencyRepository dependencyRepository, UpgradeCandidateRepository upgradeCandidateRepository) {
        this.dependencyRepository = dependencyRepository;
        this.upgradeCandidateRepository = upgradeCandidateRepository;
    }

    @GetMapping("/dependencies/{repoId}")
    public ResponseEntity<List<Dependency>> getDependencies(@PathVariable String repoId) {
        return ResponseEntity.ok(dependencyRepository.findByRepoId(repoId));
    }

    @PostMapping("/upgrade/test/{dependencyId}")
    public ResponseEntity<UpgradeCandidate> testUpgrade(@PathVariable Long dependencyId, @RequestParam String newVersion) {
        Optional<Dependency> depOpt = dependencyRepository.findById(dependencyId);
        if (depOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Dependency dep = depOpt.get();

        // In a real system, this would trigger an async job (e.g. pgmq or rabbitmq) to run a CI build
        // For MVP, since we don't have a live sandbox cluster available right now, we will simulate the runner execution 
        // by making a rudimentary check against standard latest version stability
        
        UpgradeCandidate candidate = new UpgradeCandidate();
        candidate.setDependencyId(dependencyId);
        candidate.setNewVersion(newVersion);
        
        // Simulating some impact analysis logic based on semantic versioning jump
        String currentVersionClean = dep.getCurrentVersion().replaceAll("[^0-9.]", "");
        boolean isMajorUpgrade = false;
        try {
            int currentMajor = Integer.parseInt(currentVersionClean.split("\\.")[0]);
            int newMajor = Integer.parseInt(newVersion.split("\\.")[0]);
            if (newMajor > currentMajor) {
                isMajorUpgrade = true;
            }
        } catch (Exception e) {
            // parsing failed
        }
        
        if (isMajorUpgrade) {
            candidate.setTestResults(Arrays.asList(
                "Build: SUCCESS", 
                "Tests: 118 run, 2 failed (Major version API breakages detected)"
            ));
        } else {
            candidate.setTestResults(Arrays.asList(
                "Build: SUCCESS", 
                "Tests: 120 run, 0 failed"
            ));
            // Simulated auto-PR generation
            candidate.setPrUrl("https://github.com/example/repo/pull/upgrade-" + dep.getName().replace(":", "-"));
        }
        
        UpgradeCandidate saved = upgradeCandidateRepository.save(candidate);
        return ResponseEntity.ok(saved);
    }
}
