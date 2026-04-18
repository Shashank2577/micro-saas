package com.microsaas.engagementpulse.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.engagementpulse.dto.QuestionDto;
import com.microsaas.engagementpulse.dto.SurveyDto;
import com.microsaas.engagementpulse.model.Question;
import com.microsaas.engagementpulse.model.Survey;
import com.microsaas.engagementpulse.repository.SurveyRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final EntityManager entityManager;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository, EntityManager entityManager) {
        this.surveyRepository = surveyRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Survey createSurvey(SurveyDto dto) {
        UUID tenantId = TenantContext.require();
        
        Survey survey = new Survey();
        survey.setTitle(dto.title);
        survey.setDescription(dto.description);
        survey.setStatus("DRAFT");
        survey.setTenantId(tenantId);
        survey.setScheduledAt(dto.scheduledAt);

        if (dto.questions != null) {
            for (QuestionDto qDto : dto.questions) {
                Question q = new Question();
                q.setText(qDto.text);
                q.setType(qDto.type);
                q.setOrderIndex(qDto.orderIndex);
                q.setTenantId(tenantId);
                q.setSurvey(survey);
                survey.getQuestions().add(q);
            }
        }

        return surveyRepository.save(survey);
    }

    @Transactional(readOnly = true)
    public List<Survey> listSurveys() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter").setParameter("tenantId", TenantContext.require());
        return surveyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Survey getSurvey(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter").setParameter("tenantId", TenantContext.require());
        return surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    @Transactional
    public void distributeSurvey(UUID id) {
        Survey survey = getSurvey(id);
        survey.setStatus("ACTIVE");
        surveyRepository.save(survey);
        // In a real app, this would emit an event to a queue (pgmq) to send emails/SMS
    }
}
