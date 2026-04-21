package com.microsaas.featureflagai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsaas.featureflagai.domain.FlagSegment;
import com.microsaas.featureflagai.repository.FlagSegmentRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SegmentTargetingService {

    private final FlagSegmentRepository segmentRepository;

    public List<FlagSegment> getSegmentsForFlag(UUID flagId) {
        return segmentRepository.findByFlagIdAndTenantId(flagId, TenantContext.require());
    }

    public FlagSegment createSegment(UUID flagId, FlagSegment segment) {
        segment.setTenantId(TenantContext.require());
        segment.setFlagId(flagId);
        return segmentRepository.save(segment);
    }

    public boolean evaluateSegments(UUID flagId, JsonNode userContext) {
        if (userContext == null || userContext.isNull()) {
            return false;
        }

        List<FlagSegment> segments = getSegmentsForFlag(flagId);
        if (segments.isEmpty()) {
            return false;
        }

        for (FlagSegment segment : segments) {
            JsonNode conditions = segment.getConditions();
            if (conditions != null && isMatch(conditions, userContext)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatch(JsonNode conditions, JsonNode userContext) {
        Iterator<Map.Entry<String, JsonNode>> fields = conditions.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode expectedValue = field.getValue();

            JsonNode actualValue = userContext.get(key);
            if (actualValue == null || !expectedValue.asText().equals(actualValue.asText())) {
                return false;
            }
        }
        return true;
    }
}
