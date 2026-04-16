package com.microsaas.licenseguard.service;

import com.microsaas.licenseguard.domain.Dependency;
import com.microsaas.licenseguard.domain.LicenseViolation;
import com.microsaas.licenseguard.domain.Repository;
import com.microsaas.licenseguard.repository.DependencyRepository;
import com.microsaas.licenseguard.repository.LicenseViolationRepository;
import com.microsaas.licenseguard.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScanService {

    private final RepositoryRepository repositoryRepository;
    private final DependencyRepository dependencyRepository;
    private final LicenseViolationRepository violationRepository;

    @Transactional
    public Repository registerRepo(String name, String repoUrl, UUID tenantId) {
        Repository repo = Repository.builder()
                .id(UUID.randomUUID())
                .name(name)
                .repoUrl(repoUrl)
                .tenantId(tenantId)
                .build();
        return repositoryRepository.save(repo);
    }

    @Transactional
    public void scanRepo(UUID repositoryId, UUID tenantId) {
        Repository repo = repositoryRepository.findById(repositoryId).orElseThrow();
        if (!repo.getTenantId().equals(tenantId)) throw new SecurityException();

        // create 5 mock dependencies
        dependencyRepository.saveAll(List.of(
            createDependency(repositoryId, tenantId, "spring-boot", "3.2.0", "APACHE_2"),
            createDependency(repositoryId, tenantId, "lombok", "1.18.30", "MIT"),
            createDependency(repositoryId, tenantId, "jackson", "2.15.2", "APACHE_2"),
            createDependency(repositoryId, tenantId, "commons-lang3", "3.12.0", "APACHE_2"),
            createDependency(repositoryId, tenantId, "gpl-lib", "1.0.0", "GPL_3")
        ));

        repo.setLastScannedAt(Instant.now());
        repositoryRepository.save(repo);

        detectViolations(repositoryId, tenantId);
    }

    private Dependency createDependency(UUID repositoryId, UUID tenantId, String name, String version, String license) {
        return Dependency.builder()
                .id(UUID.randomUUID())
                .repositoryId(repositoryId)
                .tenantId(tenantId)
                .name(name)
                .version(version)
                .license(license)
                .build();
    }

    @Transactional
    public void detectViolations(UUID repositoryId, UUID tenantId) {
        List<Dependency> dependencies = dependencyRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId);

        for (Dependency dep : dependencies) {
            if ("GPL_2".equals(dep.getLicense()) || "GPL_3".equals(dep.getLicense())) {
                createViolation(repositoryId, tenantId, dep, "COPYLEFT_RISK", "HIGH");
            } else if ("UNKNOWN".equals(dep.getLicense())) {
                createViolation(repositoryId, tenantId, dep, "UNKNOWN_LICENSE", "MEDIUM");
            }
        }
    }

    private void createViolation(UUID repositoryId, UUID tenantId, Dependency dep, String type, String severity) {
        LicenseViolation violation = LicenseViolation.builder()
                .id(UUID.randomUUID())
                .repositoryId(repositoryId)
                .dependencyId(dep.getId())
                .tenantId(tenantId)
                .violationType(type)
                .description("Detected license violation for " + dep.getName() + " with license " + dep.getLicense())
                .severity(severity)
                .status("OPEN")
                .build();
        violationRepository.save(violation);
    }
}
