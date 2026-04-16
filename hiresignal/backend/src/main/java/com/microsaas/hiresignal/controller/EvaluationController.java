package com.microsaas.hiresignal.controller;

import com.microsaas.hiresignal.model.Evaluation;
import com.microsaas.hiresignal.model.HireOutcome;
import com.microsaas.hiresignal.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<Evaluation> submitEval(@RequestBody SubmitEvalRequest request) {
        return ResponseEntity.ok(evaluationService.submitEval(
                request.candidateId(),
                request.requisitionId(),
                request.evaluatorId(),
                request.scorecard(),
                request.consistencyScore(),
                request.recommendation()
        ));
    }

    @PostMapping("/outcome")
    public ResponseEntity<HireOutcome> recordOutcome(@RequestBody RecordOutcomeRequest request) {
        return ResponseEntity.ok(evaluationService.recordOutcome(
                request.candidateId(),
                request.hired(),
                request.retentionMonths(),
                request.performanceScore()
        ));
    }

    public record SubmitEvalRequest(
            UUID candidateId,
            UUID requisitionId,
            UUID evaluatorId,
            String scorecard,
            Integer consistencyScore,
            Evaluation.Recommendation recommendation
    ) {}

    public record RecordOutcomeRequest(
            UUID candidateId,
            boolean hired,
            Integer retentionMonths,
            Integer performanceScore
    ) {}
}
