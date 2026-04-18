package com.microsaas.prospectiq.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.prospectiq.dto.SignalRequest;
import com.microsaas.prospectiq.model.Signal;
import com.microsaas.prospectiq.repository.SignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignalService {
    private final SignalRepository signalRepository;

    @Transactional(readOnly = true)
    public List<Signal> getSignalsForProspect(UUID prospectId) {
        UUID tenantId = TenantContext.require();
        return signalRepository.findByTenantIdAndProspectIdOrderByDetectedAtDesc(tenantId, prospectId);
    }

    @Transactional
    public Signal ingestSignal(SignalRequest request) {
        UUID tenantId = TenantContext.require();
        Signal signal = Signal.builder()
                .tenantId(tenantId)
                .prospectId(request.getProspectId())
                .type(request.getType())
                .source(request.getSource())
                .content(request.getContent())
                .detectedAt(request.getDetectedAt())
                .build();
        return signalRepository.save(signal);
    }
}
