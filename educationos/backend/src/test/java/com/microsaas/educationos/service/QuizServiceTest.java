package com.microsaas.educationos.service;

import com.microsaas.educationos.domain.entity.Module;
import com.microsaas.educationos.domain.entity.Quiz;
import com.microsaas.educationos.domain.repository.ModuleRepository;
import com.microsaas.educationos.domain.repository.QuizRepository;
import com.microsaas.educationos.domain.repository.QuestionRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;
    
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private QuizService service;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGenerateQuiz() {
        UUID moduleId = UUID.randomUUID();
        Module module = new Module();
        module.setId(moduleId);
        module.setTitle("Test Module");

        when(moduleRepository.findByIdAndTenantId(moduleId, tenantId)).thenReturn(Optional.of(module));
        
        Quiz mockQuiz = new Quiz();
        mockQuiz.setTitle("Quiz on Test Module");
        mockQuiz.setModule(module);
        when(quizRepository.save(any(Quiz.class))).thenReturn(mockQuiz);

        Quiz result = service.generateQuizForModule(moduleId);
        assertNotNull(result);
        assertEquals("Quiz on Test Module", result.getTitle());
    }
}
