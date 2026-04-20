package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.model.TicketPattern;
import com.microsaas.supportintelligence.repository.TicketPatternRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatternMiningService {

    private final TicketPatternRepository patternRepository;

    @Transactional
    public void minePatterns(UUID tenantId) {
        log.info("Mining ticket patterns for tenant: {}", tenantId);
        // Mocking pattern mining logic
        TicketPattern pattern = TicketPattern.builder()
                .tenantId(tenantId)
                .patternType("Login Issue")
                .occurrenceCount(15)
                .firstSeen(ZonedDateTime.now().minusDays(2))
                .lastSeen(ZonedDateTime.now())
                .build();
        patternRepository.save(pattern);
    }

    @Transactional(readOnly = true)
    public List<TicketPattern> getAllPatterns(UUID tenantId) {
        return patternRepository.findByTenantId(tenantId);
    }
}
