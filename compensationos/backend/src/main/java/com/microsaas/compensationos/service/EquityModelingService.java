package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.EquityGrantCalculationRequest;
import com.microsaas.compensationos.dto.EquityGrantCalculationResponse;
import com.microsaas.compensationos.entity.EquityModel;
import com.microsaas.compensationos.repository.EquityModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquityModelingService {

    private final EquityModelRepository equityModelRepository;

    public List<EquityModel> getEquityModels() {
        UUID tenantId = TenantContext.require();
        return equityModelRepository.findByTenantId(tenantId);
    }

    public EquityGrantCalculationResponse calculateGrant(EquityGrantCalculationRequest request) {
        UUID tenantId = TenantContext.require();
        EquityModel model = equityModelRepository.findById(request.getPlanId())
                .filter(m -> m.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Equity plan not found"));

        EquityGrantCalculationResponse response = new EquityGrantCalculationResponse();
        response.setTotalShares(request.getShares());
        response.setTotalValue(new BigDecimal(request.getShares()).multiply(model.getCurrentValuation()));

        List<EquityGrantCalculationResponse.VestingEvent> schedule = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(request.getVestingStartDate());
        
        long cliffShares = request.getShares() * model.getCliffMonths() / model.getVestingScheduleMonths();
        LocalDate cliffDate = startDate.plusMonths(model.getCliffMonths());

        EquityGrantCalculationResponse.VestingEvent cliffEvent = new EquityGrantCalculationResponse.VestingEvent();
        cliffEvent.setDate(cliffDate.toString());
        cliffEvent.setSharesVesting(cliffShares);
        cliffEvent.setValueVesting(new BigDecimal(cliffShares).multiply(model.getCurrentValuation()));
        schedule.add(cliffEvent);

        long monthlyShares = request.getShares() / model.getVestingScheduleMonths();
        for (int i = model.getCliffMonths() + 1; i <= model.getVestingScheduleMonths(); i++) {
            EquityGrantCalculationResponse.VestingEvent event = new EquityGrantCalculationResponse.VestingEvent();
            event.setDate(startDate.plusMonths(i).toString());
            event.setSharesVesting(monthlyShares);
            event.setValueVesting(new BigDecimal(monthlyShares).multiply(model.getCurrentValuation()));
            schedule.add(event);
        }

        response.setVestingSchedule(schedule);
        return response;
    }
}
