package com.microsaas.deploysignal.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/github")
public class GithubWebhookController {

    @PostMapping
    public ResponseEntity<String> handleGithubWebhook(
            @RequestHeader("X-GitHub-Event") String event,
            @RequestBody Map<String, Object> payload) {
        
        if ("workflow_run".equals(event)) {
            // Process workflow run event for deployment
            System.out.println("Received GitHub workflow_run event");
        }
        
        return ResponseEntity.ok("Received");
    }
}
