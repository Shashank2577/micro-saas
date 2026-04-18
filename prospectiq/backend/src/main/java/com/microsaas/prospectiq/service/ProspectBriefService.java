package com.microsaas.prospectiq.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.prospectiq.client.LiteLLMClient;
import com.microsaas.prospectiq.model.Prospect;
import com.microsaas.prospectiq.model.ProspectBrief;
import com.microsaas.prospectiq.model.Signal;
import com.microsaas.prospectiq.repository.ProspectBriefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProspectBriefService {

    private final ProspectBriefRepository briefRepository;
    private final ProspectService prospectService;
    private final SignalService signalService;
    private final LiteLLMClient liteLLMClient;

    @Transactional(readOnly = true)
    public ProspectBrief getLatestBrief(UUID prospectId) {
        UUID tenantId = TenantContext.require();
        return briefRepository.findFirstByTenantIdAndProspectIdOrderByCreatedAtDesc(tenantId, prospectId)
                .orElseThrow(() -> new RuntimeException("Brief not found"));
    }

    @Transactional
    public ProspectBrief generateBrief(UUID prospectId) {
        UUID tenantId = TenantContext.require();
        Prospect prospect = prospectService.getProspect(prospectId);
        List<Signal> signals = signalService.getSignalsForProspect(prospectId);

        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a sales brief for prospect: ").append(prospect.getName()).append("\n");
        prompt.append("Domain: ").append(prospect.getDomain()).append("\n");
        prompt.append("Industry: ").append(prospect.getIndustry()).append("\n");
        prompt.append("Signals:\n");
        for (Signal s : signals) {
            prompt.append("- [").append(s.getType()).append("] ").append(s.getContent())
                  .append(" (Source: ").append(s.getSource()).append(")\n");
        }

        String generatedContent = liteLLMClient.generateBrief(prompt.toString());

        ProspectBrief brief = ProspectBrief.builder()
                .tenantId(tenantId)
                .prospectId(prospectId)
                .content(generatedContent)
                .status("GENERATED")
                .build();
        return briefRepository.save(brief);
    }
}
