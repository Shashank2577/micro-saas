package com.microsaas.hiresignal.controller;

import com.microsaas.hiresignal.model.Candidate;
import com.microsaas.hiresignal.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<Candidate> addCandidate(@RequestBody AddCandidateRequest request) {
        return ResponseEntity.ok(candidateService.addCandidate(request.name(), request.email(), request.source(), request.requisitionId()));
    }

    @PostMapping("/{id}/score")
    public ResponseEntity<Candidate> scoreFit(@PathVariable UUID id) {
        return ResponseEntity.ok(candidateService.scoreFit(id));
    }

    @PostMapping("/{id}/advance")
    public ResponseEntity<Candidate> advance(@PathVariable UUID id) {
        return ResponseEntity.ok(candidateService.advance(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Candidate> reject(@PathVariable UUID id) {
        return ResponseEntity.ok(candidateService.reject(id));
    }

    @GetMapping("/requisition/{requisitionId}")
    public ResponseEntity<List<Candidate>> listByReq(@PathVariable UUID requisitionId) {
        return ResponseEntity.ok(candidateService.listByReq(requisitionId));
    }

    public record AddCandidateRequest(String name, String email, Candidate.Source source, UUID requisitionId) {}
}
