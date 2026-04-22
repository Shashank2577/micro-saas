package com.microsaas.auditvault.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.model.Control;
import com.microsaas.auditvault.model.Evidence;
import com.microsaas.auditvault.model.EvidenceMapping;
import com.microsaas.auditvault.repository.ControlRepository;
import com.microsaas.auditvault.repository.EvidenceMappingRepository;
import com.microsaas.auditvault.repository.EvidenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiMappingService {

    private final AiService aiService;
    private final ControlRepository controlRepository;
    private final EvidenceRepository evidenceRepository;
    private final EvidenceMappingRepository mappingRepository;
    private final EvidenceService evidenceService; // Use service instead of repo if helpful

    @Transactional
    public EvidenceMapping mapEvidence(UUID evidenceId, UUID frameworkId) {
        UUID tenantId = TenantContext.require();
        Evidence evidence = evidenceService.getEvidence(evidenceId);
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
}
