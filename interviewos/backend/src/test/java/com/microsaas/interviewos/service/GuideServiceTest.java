package com.microsaas.interviewos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.interviewos.model.InterviewGuide;
import com.microsaas.interviewos.model.InterviewType;
import com.microsaas.interviewos.repository.InterviewGuideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GuideServiceTest {

    private InterviewGuideRepository guideRepository;
    private ObjectMapper objectMapper;
    private GuideService guideService;

    @BeforeEach
    void setUp() {
        guideRepository = mock(InterviewGuideRepository.class);
        objectMapper = new ObjectMapper();
        guideService = new GuideService(guideRepository, objectMapper);
    }

    @Test
    void testGenerateGuide() throws JsonProcessingException {
        UUID tenantId = UUID.randomUUID();
        when(guideRepository.save(any(InterviewGuide.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InterviewGuide guide = guideService.generateGuide("Software Engineer", InterviewType.TECHNICAL, tenantId);

        assertNotNull(guide);
        assertEquals("Software Engineer", guide.getRoleTitle());
        assertEquals(InterviewType.TECHNICAL, guide.getInterviewType());
        assertEquals(tenantId, guide.getTenantId());
        assertNotNull(guide.getQuestions());
        assertTrue(guide.getQuestions().contains("Mock TECHNICAL question"));

        ArgumentCaptor<InterviewGuide> captor = ArgumentCaptor.forClass(InterviewGuide.class);
        verify(guideRepository).save(captor.capture());
        InterviewGuide savedGuide = captor.getValue();
        assertEquals(guide.getId(), savedGuide.getId());
    }

    @Test
    void testGenerateGuideBehavioral() throws JsonProcessingException {
        UUID tenantId = UUID.randomUUID();
        when(guideRepository.save(any(InterviewGuide.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InterviewGuide guide = guideService.generateGuide("Product Manager", InterviewType.BEHAVIORAL, tenantId);

        assertNotNull(guide);
        assertEquals("Product Manager", guide.getRoleTitle());
        assertEquals(InterviewType.BEHAVIORAL, guide.getInterviewType());
        assertEquals(tenantId, guide.getTenantId());
        assertNotNull(guide.getQuestions());
        assertTrue(guide.getQuestions().contains("Mock BEHAVIORAL question"));
        
        // Assert we have exactly 5 questions generated via ObjectMapper deserialization check
        String[] questionsArray = objectMapper.readValue(guide.getQuestions(), String[].class);
        assertEquals(5, questionsArray.length);
    }
}
