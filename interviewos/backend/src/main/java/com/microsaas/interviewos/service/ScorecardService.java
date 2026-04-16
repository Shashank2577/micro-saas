package com.microsaas.interviewos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.interviewos.model.InterviewScorecard;
import com.microsaas.interviewos.repository.InterviewScorecardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScorecardService {
    
    private final InterviewScorecardRepository scorecardRepository;
    private final ObjectMapper objectMapper;
    private final ConsistencyService consistencyService;
    
    @Transactional
    public InterviewScorecard submitScorecard(UUID guideId, UUID candidateId, UUID interviewerId, String scoresJson, String notes, UUID tenantId) {
        int overallScore = calculateOverallScore(scoresJson);
        
        InterviewScorecard scorecard = InterviewScorecard.builder()
                .id(UUID.randomUUID())
                .guideId(guideId)
                .candidateId(candidateId)
                .interviewerId(interviewerId)
                .scores(scoresJson)
                .overallScore(overallScore)
                .notes(notes)
                .tenantId(tenantId)
                .submittedAt(LocalDateTime.now())
                .build();
                
        InterviewScorecard saved = scorecardRepository.save(scorecard);
        consistencyService.recalculateConsistency(interviewerId, tenantId);
        return saved;
    }
    
    private int calculateOverallScore(String scoresJson) {
        try {
            Map<String, Integer> scores = objectMapper.readValue(scoresJson, new TypeReference<Map<String, Integer>>() {});
            if (scores.isEmpty()) {
                return 0;
            }
            double sum = 0;
            for (Integer score : scores.values()) {
                sum += score;
            }
            return (int) Math.round(sum / scores.size());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse scores JSON", e);
        }
    }
}
