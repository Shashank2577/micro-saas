package com.microsaas.callintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.callintelligence.domain.call.Call;
import com.microsaas.callintelligence.domain.call.CallRepository;
import com.microsaas.callintelligence.domain.call.CallStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CallService {

    private final CallRepository callRepository;
    private final AnalysisService analysisService;

    public CallService(CallRepository callRepository, AnalysisService analysisService) {
        this.callRepository = callRepository;
        this.analysisService = analysisService;
    }

    @Transactional
    public Call createCall(String title, String repId, String audioUrl, Integer durationSeconds) {
        UUID tenantId = TenantContext.require();
        Call call = Call.builder()
                .tenantId(tenantId)
                .title(title)
                .repId(repId)
                .audioUrl(audioUrl)
                .durationSeconds(durationSeconds)
                .status(CallStatus.UPLOADING)
                .build();
        return callRepository.save(call);
    }

    @Transactional(readOnly = true)
    public Page<Call> getCalls(String repId, Pageable pageable) {
        UUID tenantId = TenantContext.require();
        if (repId != null && !repId.isEmpty()) {
            return callRepository.findByTenantIdAndRepId(tenantId, repId, pageable);
        }
        return callRepository.findByTenantId(tenantId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Call> getCall(UUID id) {
        UUID tenantId = TenantContext.require();
        return callRepository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Call updateCallStatus(UUID id, CallStatus status) {
        UUID tenantId = TenantContext.require();
        Call call = callRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Call not found"));
        call.setStatus(status);
        return callRepository.save(call);
    }
    
    @Transactional
    public void startAnalysis(UUID callId) {
        UUID tenantId = TenantContext.require();
        Call call = callRepository.findByIdAndTenantId(callId, tenantId)
                .orElseThrow(() -> new RuntimeException("Call not found"));
        call.setStatus(CallStatus.TRANSCRIBING);
        callRepository.save(call);
        
        // Asynchronously process the call
        analysisService.analyzeCallAsync(callId, tenantId);
    }
}
