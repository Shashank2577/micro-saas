package com.microsaas.runwaymodeler.service;

import com.microsaas.runwaymodeler.model.FundingRound;
import com.microsaas.runwaymodeler.model.HeadcountScenario;
import com.microsaas.runwaymodeler.model.RunwayModel;
import com.microsaas.runwaymodeler.repository.FundingRoundRepository;
import com.microsaas.runwaymodeler.repository.HeadcountScenarioRepository;
import com.microsaas.runwaymodeler.repository.RunwayModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScenarioService {
    private final HeadcountScenarioRepository headcountScenarioRepository;
    private final FundingRoundRepository fundingRoundRepository;
    private final RunwayModelRepository runwayModelRepository;

    @Transactional
    public HeadcountScenario addHeadcountScenario(UUID modelId, String name, int additionalHeads, BigDecimal monthlyCostPerHead, LocalDate startDate, UUID tenantId) {
        RunwayModel model = runwayModelRepository.findById(modelId).orElseThrow(() -> new IllegalArgumentException("Model not found"));
        if (!model.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Model does not belong to tenant");
        }

        HeadcountScenario scenario = new HeadcountScenario();
        scenario.setId(UUID.randomUUID());
        scenario.setModelId(modelId);
        scenario.setName(name);
        scenario.setAdditionalHeads(additionalHeads);
        scenario.setMonthlyCostPerHead(monthlyCostPerHead);
        scenario.setStartDate(startDate);
        scenario.setTenantId(tenantId);

        BigDecimal impactCost = new BigDecimal(additionalHeads).multiply(monthlyCostPerHead);
        if (model.getCurrentBurn().compareTo(BigDecimal.ZERO) == 0) {
            scenario.setImpactOnRunwayDays(0);
        } else {
             int impact = impactCost.divide(model.getCurrentBurn(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(30)).intValue();
             scenario.setImpactOnRunwayDays(-impact);
        }

        return headcountScenarioRepository.save(scenario);
    }

    @Transactional
    public FundingRound addFundingRound(UUID modelId, BigDecimal amount, BigDecimal valuationCap, Double dilutionPercent, LocalDate expectedCloseDate, UUID tenantId) {
        RunwayModel model = runwayModelRepository.findById(modelId).orElseThrow(() -> new IllegalArgumentException("Model not found"));
        if (!model.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Model does not belong to tenant");
        }

        FundingRound round = new FundingRound();
        round.setId(UUID.randomUUID());
        round.setModelId(modelId);
        round.setAmount(amount);
        round.setValuationCap(valuationCap);
        round.setDilutionPercent(dilutionPercent);
        round.setExpectedCloseDate(expectedCloseDate);
        round.setTenantId(tenantId);
        
        return fundingRoundRepository.save(round);
    }

    public List<HeadcountScenario> listScenarios(UUID modelId, UUID tenantId) {
        return headcountScenarioRepository.findByModelIdAndTenantId(modelId, tenantId);
    }
}
