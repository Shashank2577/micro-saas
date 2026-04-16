package com.microsaas.socialintelligence.service;

import com.microsaas.socialintelligence.domain.model.PostDraft;
import com.microsaas.socialintelligence.domain.repository.PostDraftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostGenerationService {

    private final PostDraftRepository repository;
    private final ChatClient chatClient;

    public PostDraft generatePost(String platform, String topic) {
        String prompt = String.format("Write a catchy, on-brand social media post for %s about %s. Keep it under 280 characters if Twitter.", platform, topic);
        
        String generatedContent = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        PostDraft draft = PostDraft.builder()
                .id(UUID.randomUUID())
                .content(generatedContent)
                .platform(platform)
                .optimalPostTime(Instant.now().plus(2, ChronoUnit.HOURS))
                .predictedEngagement(BigDecimal.valueOf(Math.random() * 100))
                .status("DRAFT")
                .createdAt(Instant.now())
                .build();

        return repository.save(draft);
    }
}
