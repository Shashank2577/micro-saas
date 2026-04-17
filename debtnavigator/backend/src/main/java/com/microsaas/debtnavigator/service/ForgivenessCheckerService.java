package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.dto.ForgivenessEligibilityDto;
import com.microsaas.debtnavigator.entity.Debt;
import com.microsaas.debtnavigator.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForgivenessCheckerService {

    private final DebtRepository debtRepository;

    public List<ForgivenessEligibilityDto> checkEligibility(String tenantId) {
        List<Debt> debts = debtRepository.findByTenantId(tenantId);
        List<ForgivenessEligibilityDto> results = new ArrayList<>();

        for (Debt debt : debts) {
            if ("STUDENT_LOAN".equalsIgnoreCase(debt.getType())) {
                results.add(ForgivenessEligibilityDto.builder()
                        .debtId(debt.getId())
                        .debtName(debt.getName())
                        .isEligible(true)
                        .programName("Public Service Loan Forgiveness (PSLF)")
                        .requirements("Must be employed by a U.S. federal, state, local, or tribal government or not-for-profit organization. Must make 120 qualifying monthly payments under a qualifying repayment plan.")
                        .build());
            }
        }
        return results;
    }
}
