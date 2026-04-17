package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.FundingRound;
import com.microsaas.equityintelligence.repositories.FundingRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FundingRoundService {

    private final FundingRoundRepository fundingRoundRepository;

    public FundingRound createFundingRound(FundingRound round, UUID tenantId) {
        round.setTenantId(tenantId);
        if (round.getClosedAt() == null) {
            round.setClosedAt(LocalDateTime.now());
        }
        return fundingRoundRepository.save(round);
    }

    public List<FundingRound> getFundingRounds(UUID tenantId) {
        return fundingRoundRepository.findAllByTenantIdOrderByClosedAtAsc(tenantId);
    }
}
