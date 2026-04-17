package com.microsaas.dataroomai.controller;

import com.microsaas.dataroomai.domain.InvestorSession;
import com.microsaas.dataroomai.service.InvestorSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/data-rooms/{roomId}/sessions")
@RequiredArgsConstructor
public class InvestorSessionController {

    private final InvestorSessionService investorSessionService;

    @GetMapping
    public ResponseEntity<List<InvestorSession>> getSessions(
            @PathVariable UUID roomId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(investorSessionService.getInvestorSessions(roomId, tenantId));
    }

    @PostMapping
    public ResponseEntity<InvestorSession> createSession(
            @RequestBody InvestorSession session,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        session.setTenantId(tenantId);
        return ResponseEntity.ok(investorSessionService.createInvestorSession(session));
    }
}
