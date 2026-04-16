package com.microsaas.runwaymodeler.service;

import com.microsaas.runwaymodeler.model.RunwayModel;
import com.microsaas.runwaymodeler.model.RunwayProjection;
import com.microsaas.runwaymodeler.repository.RunwayModelRepository;
import com.microsaas.runwaymodeler.repository.RunwayProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectionService {
    private final RunwayProjectionRepository projectionRepository;
    private final RunwayModelRepository runwayModelRepository;

    @Transactional
    public List<RunwayProjection> generate12MonthProjection(UUID modelId, UUID tenantId) {
        RunwayModel model = runwayModelRepository.findById(modelId).orElseThrow(() -> new IllegalArgumentException("Model not found"));
        if (!model.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Model does not belong to tenant");
        }

        List<RunwayProjection> projections = new ArrayList<>();
        BigDecimal currentCash = model.getCurrentCash();
        BigDecimal currentBurn = model.getCurrentBurn();
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

        for (int i = 0; i < 12; i++) {
            RunwayProjection projection = new RunwayProjection();
            projection.setId(UUID.randomUUID());
            projection.setModelId(modelId);
            projection.setMonth(currentMonth.plusMonths(i));
            
            // For the first month, use current cash. For subsequent months, subtract burn.
            if (i > 0) {
                currentCash = currentCash.subtract(currentBurn);
            }
            
            projection.setProjectedCash(currentCash);
            projection.setProjectedBurn(currentBurn);
            projection.setTenantId(tenantId);
            
            projections.add(projectionRepository.save(projection));
        }
        
        return projections;
    }
}
