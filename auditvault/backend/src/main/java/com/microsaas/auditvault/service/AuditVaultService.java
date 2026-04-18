package com.microsaas.auditvault.service;

import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.dto.EvidenceIngestRequest;
import com.microsaas.auditvault.dto.PackageGenerateRequest;
import com.microsaas.auditvault.model.*;
import com.microsaas.auditvault.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditVaultService {
    private final FrameworkRepository frameworkRepository;
    private final ControlRepository controlRepository;
    private final EvidenceRepository evidenceRepository;
    private final EvidenceMappingRepository mappingRepository;
    private final AuditPackageRepository packageRepository;
    private final AiService aiService;

    public List<Framework> listFrameworks() {
        return frameworkRepository.findByTenantId(TenantContext.require());
    }

    public List<Control> listControls(UUID frameworkId) {
        return controlRepository.findByTenantIdAndFrameworkId(TenantContext.require(), frameworkId);
    }

    @Transactional
    public Evidence ingestEvidence(EvidenceIngestRequest request) {
        Evidence evidence = Evidence.builder()
                .tenantId(TenantContext.require())
                .sourceApp(request.getSourceApp())
                .evidenceType(request.getEvidenceType())
                .content(request.getContent())
                .url(request.getUrl())
                .status("PENDING_MAPPING")
                .collectedAt(OffsetDateTime.now())
                .build();
        return evidenceRepository.save(evidence);
    }

    public List<Evidence> listEvidence() {
        return evidenceRepository.findByTenantId(TenantContext.require());
    }

    public Evidence getEvidence(UUID id) {
        Evidence evidence = evidenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evidence not found"));
        if (!evidence.getTenantId().equals(TenantContext.require())) {
            throw new IllegalArgumentException("Tenant mismatch");
        }
        return evidence;
    }

    @Transactional
    public EvidenceMapping mapEvidence(UUID evidenceId, UUID frameworkId) {
        UUID tenantId = TenantContext.require();
        Evidence evidence = getEvidence(evidenceId);
        List<Control> controls = controlRepository.findByTenantIdAndFrameworkId(tenantId, frameworkId);

        String controlsContext = controls.stream()
                .map(c -> c.getId() + " - " + c.getControlCode() + ": " + c.getTitle())
                .reduce("", (a, b) -> a + "\n" + b);

        String prompt = "You are an AI auditor. Review the following evidence and map it to one of the provided controls.\n"
                + "Evidence: " + evidence.getContent() + "\n"
                + "Controls:\n" + controlsContext + "\n"
                + "Return the control UUID and a rationale.";

        ChatResponse aiResponse = aiService.chat(new ChatRequest(
            "claude-sonnet-4-6",
            List.of(
                new ChatMessage("system", prompt),
                new ChatMessage("user", "Map evidence " + evidence.getId())
            ),
            0.7,
            1000
        ));

        // Dummy mapping for now, using the first control if it exists
        Control mappedControl = controls.isEmpty() ? null : controls.get(0);
        if (mappedControl == null) {
            throw new IllegalStateException("No controls available for framework");
        }

        EvidenceMapping mapping = EvidenceMapping.builder()
                .tenantId(tenantId)
                .evidenceId(evidenceId)
                .controlId(mappedControl.getId())
                .aiConfidenceScore(0.85)
                .aiRationale(aiResponse.content())
                .status("AUTO_MAPPED")
                .build();

        evidence.setStatus("MAPPED");
        evidenceRepository.save(evidence);

        return mappingRepository.save(mapping);
    }

    @Transactional
    public EvidenceMapping approveMapping(UUID mappingId) {
        EvidenceMapping mapping = mappingRepository.findById(mappingId)
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found"));
        if (!mapping.getTenantId().equals(TenantContext.require())) {
            throw new IllegalArgumentException("Tenant mismatch");
        }
        mapping.setStatus("APPROVED");
        return mappingRepository.save(mapping);
    }

    @Transactional
    public EvidenceMapping rejectMapping(UUID mappingId) {
        EvidenceMapping mapping = mappingRepository.findById(mappingId)
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found"));
        if (!mapping.getTenantId().equals(TenantContext.require())) {
            throw new IllegalArgumentException("Tenant mismatch");
        }
        mapping.setStatus("REJECTED");
        
        Evidence evidence = evidenceRepository.findById(mapping.getEvidenceId()).orElseThrow();
        evidence.setStatus("PENDING_MAPPING");
        evidenceRepository.save(evidence);

        return mappingRepository.save(mapping);
    }

    @Transactional
    public AuditPackage generatePackage(PackageGenerateRequest request) {
        AuditPackage pkg = AuditPackage.builder()
                .tenantId(TenantContext.require())
                .frameworkId(request.getFrameworkId())
                .name(request.getName())
                .status("READY")
                .generatedAt(OffsetDateTime.now())
                .downloadUrl("/api/v1/packages/dummy-download")
                .build();
        return packageRepository.save(pkg);
    }

    public List<AuditPackage> listPackages() {
        return packageRepository.findByTenantId(TenantContext.require());
    }

    public AuditPackage getPackage(UUID id) {
        AuditPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));
        if (!pkg.getTenantId().equals(TenantContext.require())) {
            throw new IllegalArgumentException("Tenant mismatch");
        }
        return pkg;
    }
}
