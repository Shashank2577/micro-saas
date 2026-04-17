package com.microsaas.dataqualityai.controller;

import com.microsaas.dataqualityai.model.DqTest;
import com.microsaas.dataqualityai.model.TestRun;
import com.microsaas.dataqualityai.model.Issue;
import com.microsaas.dataqualityai.repository.DqTestRepository;
import com.microsaas.dataqualityai.repository.TestRunRepository;
import com.microsaas.dataqualityai.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TestController {

    private final DqTestRepository testRepository;
    private final TestRunRepository testRunRepository;
    private final IssueRepository issueRepository;

    @PostMapping("/datasets/{id}/tests")
    public ResponseEntity<DqTest> createTest(@PathVariable String id, @RequestBody DqTest test) {
        test.setDatasetId(id);
        return ResponseEntity.ok(testRepository.save(test));
    }

    @GetMapping("/datasets/{id}/tests")
    public ResponseEntity<List<DqTest>> getTests(@PathVariable String id) {
        return ResponseEntity.ok(testRepository.findByDatasetId(id));
    }

    @PostMapping("/tests/{id}/run")
    public ResponseEntity<TestRun> runTest(@PathVariable String id) {
        DqTest test = testRepository.findById(id).orElseThrow();

        TestRun run = new TestRun();
        run.setTestId(test.getId());
        run.setExecutedAt(LocalDateTime.now());

        // Mocking run logic
        if (Math.random() > 0.5) {
            run.setStatus(TestRun.RunStatus.PASS);
            run.setObservedJson("{\"result\": \"pass\"}");
        } else {
            run.setStatus(TestRun.RunStatus.FAIL);
            run.setObservedJson("{\"result\": \"fail\", \"error\": \"Data mismatch\"}");
        }

        run = testRunRepository.save(run);

        if (run.getStatus() == TestRun.RunStatus.FAIL) {
            Issue issue = new Issue();
            issue.setTestRunId(run.getId());
            issue.setSeverity("HIGH");
            issue.setDescription("Test failed: " + test.getType());
            issueRepository.save(issue);
        }

        return ResponseEntity.ok(run);
    }
}
