package com.microsaas.cashflowai.service;

import com.microsaas.cashflowai.model.CashPosition;
import com.microsaas.cashflowai.model.Source;
import com.microsaas.cashflowai.repository.CashPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashPositionService {

    private final CashPositionRepository cashPositionRepository;

    @Transactional
    public CashPosition recordActual(LocalDate date, BigDecimal balance, UUID tenantId) {
        CashPosition position = CashPosition.builder()
                .date(date)
                .balance(balance)
                .source(Source.ACTUAL)
                .tenantId(tenantId)
                .build();
        return cashPositionRepository.save(position);
    }

    public List<CashPosition> getPositions(UUID tenantId) {
        return cashPositionRepository.findByTenantIdOrderByDateAsc(tenantId);
    }

    public BigDecimal getCurrentBalance(UUID tenantId) {
        return cashPositionRepository.findTopByTenantIdOrderByDateDesc(tenantId)
                .map(CashPosition::getBalance)
                .orElse(BigDecimal.ZERO);
    }
}
