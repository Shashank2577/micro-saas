package com.microsaas.peopleanalytics.service;

import com.crosscutting.starter.security.EncryptionService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.queue.QueueService;
import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.model.PulseSurvey;
import com.microsaas.peopleanalytics.model.SurveyResponse;
import com.microsaas.peopleanalytics.repository.PulseSurveyRepository;
import com.microsaas.peopleanalytics.repository.SurveyResponseRepository;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PulseSurveyService {
    private final PulseSurveyRepository pulseSurveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final EmployeeRepository employeeRepository;
    private final QueueService queueService;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    public List<PulseSurvey> getAllSurveys() {
        return pulseSurveyRepository.findAllByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public PulseSurvey createSurvey(String title, String description) {
        PulseSurvey survey = PulseSurvey.builder()
                .tenantId(TenantContext.getTenantId())
                .title(title)
                .description(description)
                .status("ACTIVE")
                .build();
        return pulseSurveyRepository.save(survey);
    }

    @Transactional
    public void submitResponse(UUID surveyId, UUID employeeId, Integer score, String feedback) {
        UUID tenantId = TenantContext.getTenantId();

        try {
            Map<String, Object> payload = Map.of(
                "tenantId", tenantId.toString(),
                "surveyId", surveyId.toString(),
                "employeeId", employeeId.toString(),
                "score", score,
                "feedback", feedback
            );

            queueService.enqueue("pulse-survey-processing", objectMapper.writeValueAsString(payload), 0);
            log.info("Enqueued survey response for surveyId: {}", surveyId);
        } catch (Exception e) {
            log.error("Failed to enqueue survey response", e);
            throw new RuntimeException("Submission failed");
        }
    }

    @Transactional
    public void processResponse(Map<String, Object> payload) {
        UUID tenantId = UUID.fromString((String) payload.get("tenantId"));
        UUID surveyId = UUID.fromString((String) payload.get("surveyId"));
        UUID employeeId = UUID.fromString((String) payload.get("employeeId"));
        Integer score = (Integer) payload.get("score");
        String feedback = (String) payload.get("feedback");

        PulseSurvey survey = pulseSurveyRepository.findById(surveyId).orElseThrow();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();

        SurveyResponse response = SurveyResponse.builder()
                .tenantId(tenantId)
                .survey(survey)
                .employee(employee)
                .score(score)
                .feedback(feedback != null ? encryptionService.encrypt(feedback).getBytes() : null)
                .build();

        surveyResponseRepository.save(response);
    }
}
