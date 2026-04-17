package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.PulseSurvey;
import com.microsaas.peopleanalytics.model.SurveyResponse;
import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.repository.PulseSurveyRepository;
import com.microsaas.peopleanalytics.repository.SurveyResponseRepository;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.microsaas.peopleanalytics.dto.SurveyDto;
import com.microsaas.peopleanalytics.dto.SurveyResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PulseSurveyService {

    private final PulseSurveyRepository surveyRepository;
    private final SurveyResponseRepository responseRepository;
    private final EmployeeRepository employeeRepository;

    public List<PulseSurvey> getSurveys(UUID tenantId) {
        return surveyRepository.findByTenantId(tenantId);
    }

    @Transactional
    public PulseSurvey createSurvey(UUID tenantId, SurveyDto dto) {
        PulseSurvey survey = new PulseSurvey();
        survey.setTenantId(tenantId);
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setQuestions(dto.getQuestions());
        survey.setStatus("DRAFT");
        return surveyRepository.save(survey);
    }

    @Transactional
    @org.springframework.scheduling.annotation.Async
    public PulseSurvey distributeSurvey(UUID tenantId, UUID surveyId) {
        PulseSurvey survey = surveyRepository.findById(surveyId).orElseThrow();
        if (!survey.getTenantId().equals(tenantId)) throw new RuntimeException("Unauthorized");

        survey.setStatus("ACTIVE");
        survey.setDistributedAt(LocalDateTime.now());
        survey.setExpiresAt(LocalDateTime.now().plusDays(7));
        return surveyRepository.save(survey);
    }

    @Transactional
    public SurveyResponse submitResponse(UUID tenantId, UUID surveyId, SurveyResponseDto dto) {
        PulseSurvey survey = surveyRepository.findById(surveyId).orElseThrow();
        Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElseThrow();

        SurveyResponse response = new SurveyResponse();
        response.setTenantId(tenantId);
        response.setSurvey(survey);
        response.setEmployee(employee);
        response.setResponses(dto.getResponses());
        response.setSubmittedAt(LocalDateTime.now());
        return responseRepository.save(response);
    }

    public Map<String, Object> analyzeSurvey(UUID tenantId, UUID surveyId) {
        List<SurveyResponse> responses = responseRepository.findByTenantIdAndSurveyId(tenantId, surveyId);
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("total_responses", responses.size());
        // mock analysis
        return analysis;
    }
}
