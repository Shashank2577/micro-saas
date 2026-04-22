package com.microsaas.callintelligence.api;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.callintelligence.domain.call.CallParticipant;
import com.microsaas.callintelligence.domain.call.CallParticipantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/participants")
public class CallParticipantController {

    private final CallParticipantRepository participantRepository;

    public CallParticipantController(CallParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @GetMapping("/call/{callId}")
    public ResponseEntity<List<CallParticipant>> getParticipantsByCall(@PathVariable UUID callId) {
        UUID tenantId = TenantContext.require();
        List<CallParticipant> participants = participantRepository.findByCallIdAndTenantId(callId, tenantId);
        return ResponseEntity.ok(participants);
    }
}
