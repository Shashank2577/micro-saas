package com.microsaas.callintelligence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.microsaas.callintelligence.domain.call.CallRepository;
import com.microsaas.callintelligence.domain.call.CallSpeakerRepository;
import com.microsaas.callintelligence.domain.call.CallTranscriptRepository;
import com.microsaas.callintelligence.domain.insight.CallInsightRepository;
import com.microsaas.callintelligence.domain.scorecard.CoachingRecommendationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest(classes = CallIntelligenceApplication.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CallIntelligenceApplicationTests {

    @MockBean
    CallRepository callRepository;

    @MockBean
    CallSpeakerRepository callSpeakerRepository;
    
    @MockBean
    CallTranscriptRepository callTranscriptRepository;
    
    @MockBean
    CallInsightRepository callInsightRepository;
    
    @MockBean
    CoachingRecommendationRepository coachingRecommendationRepository;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    void contextLoads() {
    }
}
