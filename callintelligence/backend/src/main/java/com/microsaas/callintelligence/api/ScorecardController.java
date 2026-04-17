package com.microsaas.callintelligence.api;

import com.microsaas.callintelligence.service.ScorecardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scorecards")
public class ScorecardController {

    private final ScorecardService scorecardService;

    public ScorecardController(ScorecardService scorecardService) {
        this.scorecardService = scorecardService;
    }

    @GetMapping("/reps/{repId}")
    public ResponseEntity<Map<String, Object>> getRepScorecard(@PathVariable String repId) {
        return ResponseEntity.ok(scorecardService.getRepScorecard(repId));
    }
}
