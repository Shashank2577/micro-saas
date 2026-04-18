package com.microsaas.engagementpulse.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.engagementpulse.model.SurveyResponse;
import com.microsaas.engagementpulse.repository.SurveyResponseRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private SurveyResponseRepository responseRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        TenantContext.set(UUID.randomUUID());
    }

    @Test
    void getTeamAnalytics() {
        UUID teamId = UUID.randomUUID();
        SurveyResponse r1 = new SurveyResponse();
        r1.setEngagementScore(80.0);
        
        Session session = mock(Session.class);
        Filter filter = mock(Filter.class);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter(any())).thenReturn(filter);
        when(filter.setParameter(any(String.class), any())).thenReturn(filter);

        when(responseRepository.findByTeamIdAndTenantId(any(), any())).thenReturn(Collections.singletonList(r1));

        Map<String, Object> result = analyticsService.getTeamAnalytics(teamId);
        assertEquals(80.0, result.get("averageEngagementScore"));
        assertEquals(1, result.get("responseCount"));
    }
}
