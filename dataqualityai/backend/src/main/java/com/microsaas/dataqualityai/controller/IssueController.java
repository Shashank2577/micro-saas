package com.microsaas.dataqualityai.controller;

import com.microsaas.dataqualityai.model.Issue;
import com.microsaas.dataqualityai.model.TestRun;
import com.microsaas.dataqualityai.repository.IssueRepository;
import com.microsaas.dataqualityai.repository.TestRunRepository;
import com.microsaas.dataqualityai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class IssueController {

    private final IssueRepository issueRepository;
    private final TestRunRepository testRunRepository;
    private final AiService aiService;

    @GetMapping("/issues")
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok(issueRepository.findAll());
    }

    @PostMapping("/ai/suggest-fix")
    public ResponseEntity<Map<String, String>> suggestFix(@RequestBody Map<String, String> payload) {
        String issueId = payload.get("issueId");
        Issue issue = issueRepository.findById(issueId).orElseThrow();

        TestRun run = testRunRepository.findById(issue.getTestRunId()).orElse(null);
        String observed = run != null ? run != null ? run.getObservedJson() : "{}" : "{}";

        String hint = aiService.suggestFix(issue.getDescription(), observed);

        return ResponseEntity.ok(Map.of("hint", hint));
    }
}
