package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.EquityGrant;
import com.microsaas.equityintelligence.model.Stakeholder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CapTableService {

    private final StakeholderService stakeholderService;
    private final EquityGrantService equityGrantService;

    public Map<String, Object> getCapTable(UUID tenantId) {
        List<Stakeholder> stakeholders = stakeholderService.getStakeholders(tenantId);
        List<EquityGrant> allGrants = equityGrantService.getGrants(tenantId);

        long totalShares = allGrants.stream().mapToLong(EquityGrant::getShares).sum();

        List<Map<String, Object>> entries = new ArrayList<>();

        for (Stakeholder stakeholder : stakeholders) {
            long stakeholderShares = allGrants.stream()
                .filter(g -> g.getStakeholderId().equals(stakeholder.getId()))
                .mapToLong(EquityGrant::getShares)
                .sum();

            BigDecimal percentage = totalShares > 0
                ? BigDecimal.valueOf(stakeholderShares)
                    .divide(BigDecimal.valueOf(totalShares), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

            Map<String, Object> entry = new HashMap<>();
            entry.put("stakeholderId", stakeholder.getId());
            entry.put("name", stakeholder.getName());
            entry.put("type", stakeholder.getType().name());
            entry.put("shares", stakeholderShares);
            entry.put("ownershipPercentage", percentage);

            entries.add(entry);
        }

        Map<String, Object> capTable = new HashMap<>();
        capTable.put("totalShares", totalShares);
        capTable.put("entries", entries);

        return capTable;
    }
}
