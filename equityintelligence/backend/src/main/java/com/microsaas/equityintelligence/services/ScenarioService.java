package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.DilutionScenario;
import com.microsaas.equityintelligence.repositories.DilutionScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final DilutionScenarioRepository scenarioRepository;
    private final CapTableService capTableService;

    public DilutionScenario simulateDilution(Map<String, Object> request, UUID tenantId) {
        String name = (String) request.get("name");
        BigDecimal preMoneyValuation = new BigDecimal(request.get("preMoneyValuation").toString());
        BigDecimal amountRaised = new BigDecimal(request.get("amountRaised").toString());

        Map<String, Object> currentCapTable = capTableService.getCapTable(tenantId);
        long currentTotalShares = ((Number) currentCapTable.get("totalShares")).longValue();

        BigDecimal sharePrice = currentTotalShares > 0
            ? preMoneyValuation.divide(BigDecimal.valueOf(currentTotalShares), 4, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        long newShares = sharePrice.compareTo(BigDecimal.ZERO) > 0
            ? amountRaised.divide(sharePrice, RoundingMode.HALF_UP).longValue()
            : 0;

        long newTotalShares = currentTotalShares + newShares;

        List<Map<String, Object>> newEntries = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> currentEntries = (List<Map<String, Object>>) currentCapTable.get("entries");

        for (Map<String, Object> entry : currentEntries) {
            long shares = ((Number) entry.get("shares")).longValue();
            BigDecimal newPercentage = newTotalShares > 0
                ? BigDecimal.valueOf(shares)
                    .divide(BigDecimal.valueOf(newTotalShares), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

            Map<String, Object> newEntry = new HashMap<>(entry);
            newEntry.put("newOwnershipPercentage", newPercentage);
            newEntry.put("dilution", ((BigDecimal) entry.get("ownershipPercentage")).subtract(newPercentage));
            newEntries.add(newEntry);
        }

        Map<String, Object> investorsEntry = new HashMap<>();
        investorsEntry.put("name", "New Investors");
        investorsEntry.put("type", "INVESTOR");
        investorsEntry.put("shares", newShares);
        investorsEntry.put("newOwnershipPercentage", BigDecimal.valueOf(newShares)
            .divide(BigDecimal.valueOf(newTotalShares), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100)));
        newEntries.add(investorsEntry);

        Map<String, Object> scenarioData = new HashMap<>();
        scenarioData.put("preMoneyValuation", preMoneyValuation);
        scenarioData.put("amountRaised", amountRaised);
        scenarioData.put("sharePrice", sharePrice);
        scenarioData.put("oldTotalShares", currentTotalShares);
        scenarioData.put("newTotalShares", newTotalShares);
        scenarioData.put("entries", newEntries);

        DilutionScenario scenario = DilutionScenario.builder()
            .tenantId(tenantId)
            .name(name)
            .scenarioData(scenarioData)
            .createdAt(LocalDateTime.now())
            .build();

        return scenarioRepository.save(scenario);
    }

    public Map<String, Object> simulateWaterfall(BigDecimal exitValue, UUID tenantId) {
        Map<String, Object> capTable = capTableService.getCapTable(tenantId);
        long totalShares = ((Number) capTable.get("totalShares")).longValue();

        BigDecimal sharePrice = totalShares > 0
            ? exitValue.divide(BigDecimal.valueOf(totalShares), 4, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        List<Map<String, Object>> payouts = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> entries = (List<Map<String, Object>>) capTable.get("entries");

        for (Map<String, Object> entry : entries) {
            long shares = ((Number) entry.get("shares")).longValue();
            BigDecimal payout = sharePrice.multiply(BigDecimal.valueOf(shares));

            Map<String, Object> payoutEntry = new HashMap<>();
            payoutEntry.put("stakeholderId", entry.get("stakeholderId"));
            payoutEntry.put("name", entry.get("name"));
            payoutEntry.put("shares", shares);
            payoutEntry.put("payout", payout);
            payouts.add(payoutEntry);
        }

        Map<String, Object> waterfall = new HashMap<>();
        waterfall.put("exitValue", exitValue);
        waterfall.put("sharePrice", sharePrice);
        waterfall.put("payouts", payouts);

        return waterfall;
    }
}
