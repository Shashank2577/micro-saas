package com.microsaas.engagementpulse.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.engagementpulse.dto.QuestionDto;
import com.microsaas.engagementpulse.dto.SurveyDto;
import com.microsaas.engagementpulse.model.Survey;
import com.microsaas.engagementpulse.repository.SurveyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SurveyService surveyService;

    @BeforeEach
    void setUp() {
        TenantContext.set(UUID.randomUUID());
    }

    @Test
    void createSurvey() {
        SurveyDto dto = new SurveyDto();
        dto.title = "Test";
        dto.description = "Desc";
        QuestionDto q = new QuestionDto();
        q.text = "Q1";
        q.type = "RATING";
        q.orderIndex = 1;
        dto.questions = Collections.singletonList(q);

        Survey saved = new Survey();
        saved.setId(UUID.randomUUID());
        saved.setTitle("Test");
        
        when(surveyRepository.save(any(Survey.class))).thenReturn(saved);

        Survey result = surveyService.createSurvey(dto);
        assertNotNull(result);
        assertEquals("Test", result.getTitle());
        verify(surveyRepository).save(any(Survey.class));
    }
}
