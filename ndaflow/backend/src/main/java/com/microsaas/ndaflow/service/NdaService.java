package com.microsaas.ndaflow.service;

import com.microsaas.ndaflow.domain.ClauseType;
import com.microsaas.ndaflow.domain.Nda;
import com.microsaas.ndaflow.domain.NdaClause;
import com.microsaas.ndaflow.domain.NdaStatus;
import com.microsaas.ndaflow.domain.NdaType;
import com.microsaas.ndaflow.repository.NdaClauseRepository;
import com.microsaas.ndaflow.repository.NdaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NdaService {
    private final NdaRepository ndaRepository;
    private final NdaClauseRepository ndaClauseRepository;

    @Transactional
    public Nda generateNda(String title, String counterparty, NdaType ndaType, UUID tenantId) {
        Nda nda = Nda.builder()
                .title(title)
                .counterparty(counterparty)
                .ndaType(ndaType)
                .status(NdaStatus.DRAFT)
                .expiresAt(LocalDate.now().plusYears(1)) // Default 1 year validity
                .tenantId(tenantId)
                .build();
        
        Nda savedNda = ndaRepository.save(nda);
        
        createStandardClauses(savedNda.getId(), tenantId);
        
        return savedNda;
    }

    private void createStandardClauses(UUID ndaId, UUID tenantId) {
        ndaClauseRepository.save(NdaClause.builder()
                .ndaId(ndaId)
                .clauseType(ClauseType.CONFIDENTIALITY)
                .content("Standard confidentiality terms.")
                .isNegotiable(true)
                .tenantId(tenantId)
                .build());

        ndaClauseRepository.save(NdaClause.builder()
                .ndaId(ndaId)
                .clauseType(ClauseType.TERM)
                .content("This Agreement shall commence on the Effective Date.")
                .isNegotiable(true)
                .tenantId(tenantId)
                .build());

        ndaClauseRepository.save(NdaClause.builder()
                .ndaId(ndaId)
                .clauseType(ClauseType.SCOPE)
                .content("Scope of confidential information.")
                .isNegotiable(true)
                .tenantId(tenantId)
                .build());

        ndaClauseRepository.save(NdaClause.builder()
                .ndaId(ndaId)
                .clauseType(ClauseType.EXCLUSIONS)
                .content("Standard exclusions from confidential information.")
                .isNegotiable(false)
                .tenantId(tenantId)
                .build());

        ndaClauseRepository.save(NdaClause.builder()
                .ndaId(ndaId)
                .clauseType(ClauseType.GOVERNING_LAW)
                .content("Governing law is Delaware.")
                .isNegotiable(false)
                .tenantId(tenantId)
                .build());
    }

    @Transactional
    public Nda sendNda(UUID id, UUID tenantId) {
        Nda nda = ndaRepository.findById(id).orElseThrow();
        if (!nda.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("NDA does not belong to tenant");
        }
        nda.setStatus(NdaStatus.SENT);
        return ndaRepository.save(nda);
    }

    @Transactional
    public Nda executeNda(UUID id, UUID tenantId) {
        Nda nda = ndaRepository.findById(id).orElseThrow();
        if (!nda.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("NDA does not belong to tenant");
        }
        nda.setStatus(NdaStatus.EXECUTED);
        return ndaRepository.save(nda);
    }

    public List<Nda> listNdas(UUID tenantId) {
        return ndaRepository.findByTenantId(tenantId);
    }

    public List<Nda> listExpiringSoon(UUID tenantId, int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.plusDays(daysAhead);
        return ndaRepository.findExpiringSoon(tenantId, today, expiryDate);
    }
}
