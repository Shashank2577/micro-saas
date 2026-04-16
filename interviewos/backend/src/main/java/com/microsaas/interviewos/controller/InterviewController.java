package com.microsaas.interviewos.controller;

import com.microsaas.interviewos.model.EvaluatorConsistency;
import com.microsaas.interviewos.model.InterviewGuide;
import com.microsaas.interviewos.model.InterviewScorecard;
import com.microsaas.interviewos.model.InterviewType;
import com.microsaas.interviewos.service.ConsistencyService;
import com.microsaas.interviewos.service.GuideService;
import com.microsaas.interviewos.service.ScorecardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InterviewController {

    private final GuideService guideService;
    private final ScorecardService scorecardService;
    private final ConsistencyService consistencyService;

    @PostMapping("/guides")
    public ResponseEntity<InterviewGuide> generateGuide(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody GenerateGuideRequest request) {
        return ResponseEntity.ok(guideService.generateGuide(request.getRoleTitle(), request.getInterviewType(), tenantId));
    }

    @GetMapping("/guides")
    public ResponseEntity<List<InterviewGuide>> listGuides(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(guideService.getGuides(tenantId));
    }

    @PostMapping("/scorecards")
    public ResponseEntity<InterviewScorecard> submitScorecard(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody SubmitScorecardRequest request) {
        return ResponseEntity.ok(scorecardService.submitScorecard(
                request.getGuideId(),
                request.getCandidateId(),
                request.getInterviewerId(),
                request.getScores(),
                request.getNotes(),
                tenantId
        ));
    }

    @PostMapping("/consistency/{interviewerId}/recalculate")
    public ResponseEntity<EvaluatorConsistency> recalculateConsistency(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID interviewerId) {
        return ResponseEntity.ok(consistencyService.recalculateConsistency(interviewerId, tenantId));
    }

    @Data
    public static class GenerateGuideRequest {
        private String roleTitle;
        private InterviewType interviewType;
    }

    @Data
    public static class SubmitScorecardRequest {
        private UUID guideId;
        private UUID candidateId;
        private UUID interviewerId;
        private String scores;
        private String notes;
    }
}
