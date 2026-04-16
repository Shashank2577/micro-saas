package com.microsaas.interviewos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.interviewos.model.InterviewGuide;
import com.microsaas.interviewos.model.InterviewType;
import com.microsaas.interviewos.repository.InterviewGuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuideService {
    
    private final InterviewGuideRepository guideRepository;
    private final ObjectMapper objectMapper;
    
    public InterviewGuide generateGuide(String roleTitle, InterviewType type, UUID tenantId) {
        List<String> questions = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            questions.add("Mock " + type.name() + " question " + i + " for " + roleTitle);
        }
        
        String questionsJson;
        try {
            questionsJson = objectMapper.writeValueAsString(questions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize questions", e);
        }
        
        InterviewGuide guide = InterviewGuide.builder()
                .id(UUID.randomUUID())
                .roleTitle(roleTitle)
                .interviewType(type)
                .questions(questionsJson)
                .tenantId(tenantId)
                .createdAt(LocalDateTime.now())
                .build();
                
        return guideRepository.save(guide);
    }
    
    public List<InterviewGuide> getGuides(UUID tenantId) {
        return guideRepository.findByTenantId(tenantId);
    }
}
