package com.microsaas.callintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.callintelligence.domain.call.*;
import com.microsaas.callintelligence.domain.scorecard.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ScorecardService {

    private final CallRepository callRepository;
    private final CallSpeakerRepository speakerRepository;
    private final CoachingRecommendationRepository recommendationRepository;

    public ScorecardService(CallRepository callRepository, CallSpeakerRepository speakerRepository, CoachingRecommendationRepository recommendationRepository) {
        this.callRepository = callRepository;
        this.speakerRepository = speakerRepository;
        this.recommendationRepository = recommendationRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getRepScorecard(String repId) {
        UUID tenantId = TenantContext.require();
        List<Call> calls = callRepository.findByTenantIdAndRepId(tenantId, repId);
        
        int totalCalls = calls.size();
        BigDecimal avgTalkRatio = BigDecimal.ZERO;
        
        if (totalCalls > 0) {
            BigDecimal totalRatio = BigDecimal.ZERO;
            int count = 0;
            for (Call call : calls) {
                List<CallSpeaker> speakers = speakerRepository.findByCallIdAndTenantId(call.getId(), tenantId);
                for (CallSpeaker s : speakers) {
                    if ("REP".equals(s.getRole()) && s.getTalkRatio() != null) {
                        totalRatio = totalRatio.add(s.getTalkRatio());
                        count++;
                    }
                }
            }
            if (count > 0) {
                avgTalkRatio = totalRatio.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
            }
        }

        List<CoachingRecommendation> recommendations = recommendationRepository.findByRepIdAndTenantId(repId, tenantId);

        Map<String, Object> scorecard = new HashMap<>();
        scorecard.put("repId", repId);
        scorecard.put("totalCalls", totalCalls);
        scorecard.put("averageTalkRatio", avgTalkRatio);
        scorecard.put("coachingRecommendations", recommendations);
        return scorecard;
    }
}
