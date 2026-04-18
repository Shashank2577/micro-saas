package com.microsaas.engagementpulse.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.engagementpulse.ai.LiteLlmClient;
import com.microsaas.engagementpulse.dto.AnswerDto;
import com.microsaas.engagementpulse.dto.SurveyResponseDto;
import com.microsaas.engagementpulse.model.Answer;
import com.microsaas.engagementpulse.model.Question;
import com.microsaas.engagementpulse.model.Survey;
import com.microsaas.engagementpulse.model.SurveyResponse;
import com.microsaas.engagementpulse.repository.SurveyRepository;
import com.microsaas.engagementpulse.repository.SurveyResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResponseService {
    private final SurveyResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final LiteLlmClient aiClient;

    @Autowired
    public ResponseService(SurveyResponseRepository responseRepository, SurveyRepository surveyRepository, LiteLlmClient aiClient) {
        this.responseRepository = responseRepository;
        this.surveyRepository = surveyRepository;
        this.aiClient = aiClient;
    }

    @Transactional
    public SurveyResponse submitResponse(SurveyResponseDto dto) {
        UUID tenantId = TenantContext.require();
        
        Survey survey = surveyRepository.findById(dto.surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        SurveyResponse response = new SurveyResponse();
        response.setSurvey(survey);
        response.setEmployeeId(dto.employeeId);
        response.setTeamId(dto.teamId);
        response.setSubmittedAt(LocalDateTime.now());
        response.setTenantId(tenantId);

        double totalScore = 0;
        int count = 0;

        for (AnswerDto aDto : dto.answers) {
            Question question = survey.getQuestions().stream()
                    .filter(q -> q.getId().equals(aDto.questionId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            Answer answer = new Answer();
            answer.setResponse(response);
            answer.setQuestion(question);
            answer.setTenantId(tenantId);

            if ("RATING".equals(question.getType()) && aDto.ratingValue != null) {
                answer.setRatingValue(aDto.ratingValue);
                totalScore += (aDto.ratingValue * 20); // 1-5 to 20-100
                count++;
            } else if ("FREE_TEXT".equals(question.getType()) && aDto.textValue != null) {
                answer.setTextValue(aDto.textValue);
                LiteLlmClient.SentimentResult sentiment = aiClient.analyzeSentiment(aDto.textValue);
                answer.setSentimentLabel(sentiment.label);
                answer.setSentimentScore(sentiment.score);
            }
            response.getAnswers().add(answer);
        }

        if (count > 0) {
            response.setEngagementScore(totalScore / count);
        }

        return responseRepository.save(response);
    }
}
