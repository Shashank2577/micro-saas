package com.microsaas.meetingbrain.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.meetingbrain.model.Meeting;
import com.microsaas.meetingbrain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private TranscriptLineRepository transcriptLineRepository;
    @Mock
    private DecisionRepository decisionRepository;
    @Mock
    private ActionItemRepository actionItemRepository;
    @Mock
    private OpenQuestionRepository openQuestionRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AiService aiService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MeetingService meetingService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateMeeting() {
        Meeting m = new Meeting();
        m.setTitle("Test Meeting");
        
        when(meetingRepository.save(any())).thenReturn(m);
        
        Meeting result = meetingService.createMeeting(m);
        assertNotNull(result);
        assertEquals(tenantId, m.getTenantId());
        verify(meetingRepository, times(1)).save(m);
    }
}
