package com.microsaas.voiceagentbuilder.controller;

import com.microsaas.voiceagentbuilder.model.CallLog;
import com.microsaas.voiceagentbuilder.service.CallLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CallLogController {
    private final CallLogService callLogService;

    @GetMapping("/agents/{agentId}/calls")
    public List<CallLog> getAgentCallLogs(@PathVariable UUID agentId) {
        return callLogService.getCallLogs(agentId);
    }

    @GetMapping("/calls")
    public List<CallLog> getAllCallLogs() {
        return callLogService.getAllCallLogs();
    }

    @GetMapping("/calls/{id}")
    public CallLog getCallLog(@PathVariable UUID id) {
        return callLogService.getCallLog(id);
    }
}
