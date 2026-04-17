package com.microsaas.dataroomai.service;

import com.microsaas.dataroomai.domain.InvestorSession;
import com.microsaas.dataroomai.repository.InvestorSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvestorSessionService {

    private final InvestorSessionRepository investorSessionRepository;

    @Transactional(readOnly = true)
    public List<InvestorSession> getInvestorSessions(UUID roomId, UUID tenantId) {
        return investorSessionRepository.findByRoomIdAndTenantId(roomId, tenantId);
    }

    @Transactional
    public InvestorSession createInvestorSession(InvestorSession investorSession) {
        return investorSessionRepository.save(investorSession);
    }
}
