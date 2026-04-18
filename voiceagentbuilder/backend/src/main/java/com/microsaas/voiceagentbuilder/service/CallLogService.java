package com.microsaas.voiceagentbuilder.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.voiceagentbuilder.model.CallLog;
import com.microsaas.voiceagentbuilder.repository.CallLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CallLogService {
    private final CallLogRepository callLogRepository;

    public List<CallLog> getCallLogs(UUID agentId) {
        return callLogRepository.findByAgentIdAndTenantId(agentId, TenantContext.require());
    }

    public List<CallLog> getAllCallLogs() {
        return callLogRepository.findByTenantId(TenantContext.require());
    }

    public CallLog getCallLog(UUID id) {
        return callLogRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Call log not found"));
    }
}
