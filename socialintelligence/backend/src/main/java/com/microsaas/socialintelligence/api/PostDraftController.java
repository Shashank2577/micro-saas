package com.microsaas.socialintelligence.api;

import com.microsaas.socialintelligence.domain.model.PostDraft;
import com.microsaas.socialintelligence.service.PostGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostDraftController {

    private final PostGenerationService service;

    @PostMapping("/generate")
    public ResponseEntity<PostDraft> generatePost(@RequestBody Map<String, String> request) {
        String platform = request.get("platform");
        String topic = request.get("topic");
        return ResponseEntity.ok(service.generatePost(platform, topic));
    }
}
