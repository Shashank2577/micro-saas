package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.EquityGrant;
import com.microsaas.equityintelligence.model.VestingEvent;
import com.microsaas.equityintelligence.repositories.EquityGrantRepository;
import com.microsaas.equityintelligence.repositories.VestingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquityGrantService {

    private final EquityGrantRepository equityGrantRepository;
    private final VestingEventRepository vestingEventRepository;

    @Transactional
    public EquityGrant createGrant(EquityGrant grant, UUID tenantId) {
        grant.setTenantId(tenantId);
        EquityGrant savedGrant = equityGrantRepository.save(grant);
        
        generateVestingSchedule(savedGrant);
        
        return savedGrant;
    }

    public List<EquityGrant> getGrants(UUID tenantId) {
        return equityGrantRepository.findAllByTenantId(tenantId);
    }
    
    public List<EquityGrant> getGrantsByStakeholder(UUID stakeholderId, UUID tenantId) {
        return equityGrantRepository.findAllByStakeholderIdAndTenantId(stakeholderId, tenantId);
    }

    public List<VestingEvent> getVestingSchedule(UUID grantId, UUID tenantId) {
        return vestingEventRepository.findAllByGrantIdAndTenantIdOrderByVestDateAsc(grantId, tenantId);
    }

    private void generateVestingSchedule(EquityGrant grant) {
        vestingEventRepository.deleteAllByGrantIdAndTenantId(grant.getId(), grant.getTenantId());
        
        long totalShares = grant.getShares();
        int totalMonths = grant.getVestMonths();
        int cliffMonths = grant.getCliffMonths();
        
        if (totalMonths == 0) {
            VestingEvent event = VestingEvent.builder()
                .grantId(grant.getId())
                .tenantId(grant.getTenantId())
                .vestDate(grant.getGrantDate())
                .sharesVested(totalShares)
                .cumulativeVested(totalShares)
                .build();
            vestingEventRepository.save(event);
            return;
        }

        List<VestingEvent> schedule = new ArrayList<>();
        long sharesPerMonth = totalShares / totalMonths;
        long remainingShares = totalShares - (sharesPerMonth * totalMonths);
        long cumulativeVested = 0;

        for (int month = 1; month <= totalMonths; month++) {
            LocalDate vestDate = grant.getGrantDate().plusMonths(month);
            
            long sharesToVest = sharesPerMonth;
            if (month == totalMonths) {
                sharesToVest += remainingShares;
            }

            if (month < cliffMonths) {
                continue;
            } else if (month == cliffMonths) {
                sharesToVest += (sharesPerMonth * (cliffMonths - 1));
            }

            cumulativeVested += sharesToVest;
            
            VestingEvent event = VestingEvent.builder()
                .grantId(grant.getId())
                .tenantId(grant.getTenantId())
                .vestDate(vestDate)
                .sharesVested(sharesToVest)
                .cumulativeVested(cumulativeVested)
                .build();
            schedule.add(event);
        }
        
        vestingEventRepository.saveAll(schedule);
    }
}
